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

        //Spectrums llamados
        enemigos.add(new Spectrum(1, 1, 1.2, 400, 80, ANCHO_SPECTRUM, ALTO_SPECTRUM, 100.0, 300.0, 15.0));
        enemigos.add(new Spectrum(1, 1, 1.2, 200, 80, ANCHO_SPECTRUM, ALTO_SPECTRUM, 100.0, 300.0, 15.0));

        List<Point2D.Double> destinos = List.of(
                new Point2D.Double(1900, 560),
                new Point2D.Double(2100, 560),
                new Point2D.Double(2280, 560)
        );

        enemigos.add(new Arghon(5, 1, 2.0, 2100, 560, 64, 80, destinos));

        /**
         * DEBO AGREGAR LA VIDA EXTRA, OBJETO+3,EXODO
         */
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
        proyectiles.add(proyectil);
    }

    public void eliminarProyectilesInactivos() {
        for (int i = proyectiles.size() - 1; i >= 0; i--) {
            if (!proyectiles.get(i).estaActivo()) {
                proyectiles.remove(i);
            }
        }
    }

    public void reiniciarCheckpoint(int numero) {
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
        return null;
    }

    public Point2D.Double buscarPosicionSegura(Jugador jugador) {
        Rectangle2D ian = jugador.obtenerLimites();
        double y = plataformas.get(0).getY() - ALTO_SPECTRUM;
        double der = ian.getMaxX() + 100;
        double izq = ian.getMinX() - 100 - ANCHO_SPECTRUM;
        if (esZonaLibre(der, y)) {
            return new Point2D.Double(der, y);
        }
        if (esZonaLibre(izq, y)) {
            return new Point2D.Double(izq, y);
        }
        return null;
    }

    private boolean esZonaLibre(double x, double y) {
        if (x < 0 || x + ANCHO_SPECTRUM > ancho) {
            return false;
        }
        Rectangle2D area = new Rectangle2D.Double(x, y, ANCHO_SPECTRUM, ALTO_SPECTRUM);
        for (Trampa trampa : trampas) {
            if (area.intersects(trampa.obtenerLimites())) {
                return false;
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
