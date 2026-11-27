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
    private final List<PuntoControl> puntosControl = new ArrayList<>();
    private final List<ObjetoRecogible> objetos = new ArrayList<>();
    private final int ancho = 4000;
    private final int alto = 600;
    private boolean exodoCreado;


    public void cargar() {
        enemigos.clear();
        proyectiles.clear();
        plataformas.clear();
        trampas.clear();
        objetos.clear();
        puntosControl.clear();
        exodoCreado = false;

        plataformas.add(new Plataforma(0, 536, 736, 64));
        plataformas.add(new Plataforma(832, 536, 224, 64));
        plataformas.add(new Plataforma(1056, 472, 256, 128));
        plataformas.add(new Plataforma(2016, 536, 864, 64));
        plataformas.add(new Plataforma(2944, 536, 256, 64));
        plataformas.add(new Plataforma(3200, 536, 800, 64));

        trampas.add(new Trampa(600, 620, 100, 20));
        trampas.add(new Trampa(1150, 620, 100, 20));

        puntosControl.add(new PuntoControl(64, 472, 32, 64, 0, 64, 488));
        puntosControl.add(new PuntoControl(1216, 408, 32, 64, 1, 1216, 424));

        Point2D.Double inicio = getPosicionPuntosControl(0);
        jugador = new Jugador(inicio.x, inicio.y, 32, 48);

        enemigos.add(new Spectrum(1, 1, 1.2, 576, 488, ANCHO_SPECTRUM, ALTO_SPECTRUM, 100.0, 300.0, 15.0));
        enemigos.add(new Spectrum(2, 1, 1.2, 960, 488, ANCHO_SPECTRUM, ALTO_SPECTRUM, 100.0, 300.0, 15.0));
        enemigos.add(new Spectrum(3,1, 1.2, 2240, 488, ANCHO_SPECTRUM, ALTO_SPECTRUM, 100.0, 300.0, 15.0));
        enemigos.add(new Spectrum(4, 1, 1.2, 2496, 296, ANCHO_SPECTRUM, ALTO_SPECTRUM, 100.0, 300.0, 15.0 ));

        List<Point2D.Double> destinos = new ArrayList<>();
        destinos.add(new Point2D.Double(1900, 560));
        destinos.add(new Point2D.Double(2100, 560));
        destinos.add(new Point2D.Double(2280, 560));

        enemigos.add(new Arghon(5, 1, 2.0, 2100, 560, 64, 80, destinos));

        objetos.add(new ObjetoRecogible(500, 608, 32, 32, 1, ObjetoRecogible.Tipo.VIDA));
        objetos.add(new ObjetoRecogible(1500, 608, 32, 32, 2, ObjetoRecogible.Tipo.VIDA));
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

        Arghon arghon = getArghon();
        if (!exodoCreado && arghon != null && arghon.estaDerrotado()) {
            double x = arghon.getX() + arghon.getAncho() / 2.0 - 16;
            double y = plataformas.get(0).getY() - 32;
            objetos.add(new ObjetoRecogible(x, y, 32, 32, 3, ObjetoRecogible.Tipo.EXODO));
            exodoCreado = true;
        }
    }

    public boolean recogerExodo(ObjetoRecogible objeto) {
        if (objeto.getTipo() != ObjetoRecogible.Tipo.EXODO) {
            return false;
        }
        if (!objetos.contains(objeto) || !objeto.estaDisponible()) {
            return false;
        }
        Arghon arghon = getArghon();
        if (arghon == null || !arghon.estaDerrotado() || jugador.estaMuerto()) {
            return false;
        }
        return jugador.obtenerLimites().intersects(objeto.obtenerLimites());
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

    public void reiniciarPuntoControl(int numero) {
        Point2D.Double posicion = getPosicionPuntosControl(numero);
        if (posicion == null) {
            throw new IllegalArgumentException("No existe el punto de control " + numero);
        }
        jugador.reaparecer(posicion.x, posicion.y);
        proyectiles.clear();
        for (Enemigo enemigo : enemigos) {
            if (enemigo instanceof Arghon) {
                Arghon arghon = (Arghon) enemigo;
                arghon.reiniciarCombate();
            } else if (enemigo instanceof Spectrum) {
                Spectrum spectrum = (Spectrum) enemigo;
                spectrum.restablecerTrasMuerteIan();
            }
        }

        for (int i = objetos.size() - 1; i >= 0; i--) {
            if (objetos.get(i).getTipo() == ObjetoRecogible.Tipo.EXODO) {
                objetos.remove(i);
            }
        }
        exodoCreado = false;
    }

    private Point2D.Double getPosicionPuntosControl(int numero) {
        for (PuntoControl puntoControl : puntosControl) {
            if (puntoControl.getNumero() == numero) {
                return puntoControl.obtenerPosicionReaparicion();
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

    public List<PuntoControl> getPuntosControl() {
        return puntosControl;
    }

    public List<ObjetoRecogible> getObjetoRecogibles() {
        return objetos;
    }

    public Arghon getArghon() {
        for (Enemigo enemigo : enemigos) {
            if (enemigo instanceof Arghon) {
                return (Arghon) enemigo;
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