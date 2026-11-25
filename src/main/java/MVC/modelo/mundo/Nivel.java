package MVC.modelo.mundo;

import MVC.modelo.entidades.Arghon;
import MVC.modelo.entidades.Enemigo;
import MVC.modelo.entidades.Jugador;
import MVC.modelo.entidades.Proyectil;
import MVC.modelo.entidades.Spectrum;
import MVC.modelo.objetos.ObjetoRecogible;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

public class Nivel {
    private static final int ANCHO_SPECTRUM = 32;
    private static final int ALTO_SPECTRUM = 48;
    private Jugador jugador;
    private final List<Enemigo> enemigos = new ArrayList<>();
    private final List<Proyectil> proyectiles = new ArrayList<>();
    private final List<Plataforma> plataformas = new ArrayList<>();
    private final List<Trampa> trampas = new ArrayList<>();
    private final List<Checkpoint> checkpoints = new ArrayList<>();
    private final List<ObjetoRecogible> objetos = new ArrayList<>();
    private final int ancho = 2400;
    private final int alto = 720;
    private boolean exodoDisponible;

    public void cargar() {
        enemigos.clear();
        proyectiles.clear();
        plataformas.clear();
        trampas.clear();
        objetos.clear();
        checkpoints.clear();
        exodoDisponible = false;

        //por definir las coords
        //Este es el piso como tal
        plataformas.add(new Plataforma(0, 640, ancho, 80));
        plataformas.add(new Plataforma(350, 520, 180, 20));
        plataformas.add(new Plataforma(850, 500, 200, 20));
        plataformas.add(new Plataforma(1350, 520, 180, 20));

        //se pueden poner aquí plataformas de salto:
        //plataformas.add(new Plataforma(350, 520, 180, 20));
        // plataformas.add(new Plataforma(850, 500, 200, 20));

        //LO MISMO CON LAS TRAMPAS, DECIDIR DONDE VAN:
        trampas.add(new Trampa(600, 620, 100, 20));
        trampas.add(new Trampa(1150, 620, 100, 20));

        //CONFIGURACIÓN DE CHECKPointss:
        checkpoints.add(new Checkpoint(80, 580, 30, 60, 0, 80, 592));
        checkpoints.add(new Checkpoint(780, 580, 30, 60, 1, 780, 592));

        Point2D.Double inicio = getPosicionCheckpoint(0);
        jugador = new Jugador(inicio.x, inicio.y, 32, 48);

        enemigos.add(crearSpectrum(1, 400, 592));
        enemigos.add(crearSpectrum(2, 900, 592));
        enemigos.add(crearSpectrum(3, 1400, 592));
        enemigos.add(crearSpectrum(4, 1750, 592));

        List<Point2D.Double> destinos = List.of(
                new Point2D.Double(1900, 560),
                new Point2D.Double(2100, 560),
                new Point2D.Double(2280, 560)
        );

        enemigos.add(new Arghon(5, 1, 2.0, 2100, 560, 64, 80, destinos));

        /**
         * DEBO AGREGAR LA VIDA EXTRA, OBJETO+3,NEXUS
         */
    }

    private Spectrum crearSpectrum(int id, double x, double y) {
        return new Spectrum(id, 1, 1.2,
                x, y, ANCHO_SPECTRUM, ALTO_SPECTRUM, 100.0,
                300.0, 15.0);
    }

    public void actualizar(double dt) {
        jugador.actualizar(dt);
        for (Enemigo enemigo : enemigos) {
            enemigo.actualizar(dt, jugador, this);
        }
        for (Proyectil proyectil : proyectiles) {
            proyectil.actualizar(dt);
            if (proyectil.getX() + proyectil.getAncho() <= 0 ||
                    proyectil.getX() >= ancho ||
                    proyectil.getY() + proyectil.getAlto() <= 0 ||
                    proyectil.getY() >= alto) {
                proyectil.destruir();
            }
        }

    }

    public void agregarProyectil(Proyectil proyectil) {
        if (proyectil != null && proyectil.estaActivo() &&
                !proyectiles.contains(proyectil)) {
            proyectiles.add(proyectil);
        }
    }

    public void eliminarProyectilesInactivos() {
        for (int i = proyectiles.size() - 1; i >= 0; i--) {
            if (!proyectiles.get(i).estaActivo()) {
                proyectiles.remove(i);
            }
        }
    }

    public void reiniciarDesdeCheckpoint(int numero) {
        getPosicionCheckpoint(numero);
        proyectiles.clear();
        for (Enemigo enemigo : enemigos) {
            if (enemigo instanceof Arghon arghon) {
                arghon.reiniciarCombate();
            } else if (enemigo instanceof Spectrum spectrum) {
                spectrum.restablecerTrasMuerteIan();
            }
        }
        exodoDisponible = false;
    }

    private Point2D.Double getPosicionCheckpoint(int numero) {
        for (Checkpoint checkpoint : checkpoints) {
            if (checkpoint.getNumero() == numero) {
                return checkpoint.obtenerPosicionReaparicion();
            }
        }
        throw new IllegalArgumentException("No se encontro el checkpoint" + numero);
    }

    public Point2D.Double buscarPosicionSeguraRenacimiento(Jugador jugador) {
        if (jugador == null || jugador.estaMuerto()) return null;
        Point2D.Double mejorPosicion = null;
        double menorDistancia = Double.POSITIVE_INFINITY;
        double centroJugadorX = jugador.getX() + jugador.getAncho() / 2.0;
        double centroJugadorY = jugador.getY() + jugador.getAlto() / 2.0;

        for (Plataforma suelo : plataformas) {
            double y = suelo.getY() - ALTO_SPECTRUM;
            double inicioX = Math.max(0, suelo.getX());
            double finX = Math.min(ancho - ANCHO_SPECTRUM, suelo.getX() + suelo.getAncho() - ANCHO_SPECTRUM);

            if (y < 0 || y + ALTO_SPECTRUM > alto) {
                continue;
            }

            for (double x = inicioX; x <= finX; x += 1.0) {
                Rectangle2D.Double area = new Rectangle2D.Double(x, y, ANCHO_SPECTRUM, ALTO_SPECTRUM);
                if (!esZonaLibre(area, jugador)) {
                    continue;
                }
                double distancia = Point2D.distanceSq(x + ANCHO_SPECTRUM / 2.0, y + ALTO_SPECTRUM / 2.0, centroJugadorX, centroJugadorY);
                if (distancia < menorDistancia) {
                    menorDistancia = distancia;
                    mejorPosicion = new Point2D.Double(x, y);
                }
            }
        }
        return mejorPosicion;
    }

    private boolean esZonaLibre(Rectangle2D.Double area, Jugador jugador) {
        if (area.intersects(jugador.obtenerLimites())) {
            return false;
        }
        for (Trampa trampa : trampas) {
            if (area.intersects(trampa.obtenerLimites())) {
                return false;
            }
        }
        for (Plataforma plataforma : plataformas) {
            if (area.intersects(plataforma.obtenerLimites())) {
            }
        }
        for (Enemigo enemigo : enemigos) {
            if (enemigo.estaVivo() && area.intersects(enemigo.obtenerLimites())) {
                return false;
            }
        }
        return true;
    }

    public Jugador getJugador() {
        return jugador;
    }

    public List<Enemigo> getEnemigos() {
        return enemigos;
    }

    public List<Plataforma> getPlataformas() {
        return plataformas;
    }

    public List<Trampa> getTrampas() {
        return trampas;
    }

    public List<Proyectil> getProyectiles() {
        return proyectiles;
    }

    public List<Checkpoint> getCheckpoints() {
        return checkpoints;
    }

    public List<ObjetoRecogible> getObjetoRecogibles() {
        return objetos;
    }

    public Arghon getArghon() {
        for (Enemigo enemigo : enemigos) {
            if (enemigo instanceof Arghon arghon) {
                return arghon;
            }
        }
        return null;
    }

    public int getAncho() {
        return ancho;
    }

    public int getAlto() {
        return alto;
    }
}
