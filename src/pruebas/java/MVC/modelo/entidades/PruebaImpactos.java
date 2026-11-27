package MVC.modelo.entidades;

import MVC.modelo.mundo.GestorColisiones;
import MVC.modelo.mundo.Impacto;
import MVC.modelo.mundo.Nivel;
import MVC.modelo.mundo.Plataforma;

import java.awt.geom.Rectangle2D;
import java.util.List;

public class PruebaImpactos {
    public static void main(String[] args) {
        comprobarDisparoCercano(1);
        comprobarDisparoCercano(-1);
        comprobarMargenesSpectrum();
        comprobarMargenesIan(1);
        comprobarMargenesIan(-1);
        comprobarPiso();
        System.out.println("OK: salida del disparo, margenes transparentes, impactos y piso.");
    }

    private static Nivel crearNivel() {
        Nivel nivel = new Nivel();
        nivel.cargar();
        nivel.getEnemigos().clear();
        nivel.getPlataformas().clear();
        nivel.getJugador().establecerPosicion(200, 200);
        return nivel;
    }

    private static Spectrum agregarSpectrum(Nivel nivel, double x) {
        Spectrum spectrum = new Spectrum(90, 1, 1, x, 200, 32, 48, 0, 300, 15);
        nivel.getEnemigos().add(spectrum);
        return spectrum;
    }

    private static void comprobarDisparoCercano(int direccion) {
        Nivel nivel = crearNivel();
        Jugador ian = nivel.getJugador();
        ian.setDireccion(direccion);
        double xEnemigo = direccion == 1 ? ian.getX() + ian.getAncho() + 15 : ian.getX() - 32 - 15;
        Spectrum spectrum = agregarSpectrum(nivel, xEnemigo);
        Proyectil bala = ian.disparar();
        Rectangle2D cuerpo = ian.obtenerLimitesImpacto();
        comprobar(direccion == 1 ? bala.getX() == cuerpo.getMaxX()
                        : bala.getX() + bala.getAncho() == cuerpo.getMinX(),
                "La bala nace junto al cuerpo, tambien al mirar a la izquierda");
        nivel.agregarProyectil(bala);
        GestorColisiones gestor = new GestorColisiones();
        bala.actualizar(0.02);
        comprobar(gestor.detectarImpactos(nivel).isEmpty() && bala.estaActivo(),
                "El disparo cercano tiene recorrido visible antes de impactar");
        List<Impacto> impactos = List.of();
        for (int i = 0; i < 15 && impactos.isEmpty(); i++) {
            bala.actualizar(0.01);
            impactos = gestor.detectarImpactos(nivel);
        }
        comprobar(impactos.size() == 1 && impactos.get(0).getObjetivo() == spectrum,
                "El disparo alcanza el cuerpo del Spectrum en ambas direcciones");
        int salud = spectrum.getSalud();
        comprobar(bala.impactar(spectrum) && spectrum.getSalud() == salud - ian.getDanioActual(),
                "El impacto aplica el dano una vez");
        comprobar(!bala.impactar(spectrum), "Una bala consumida no vuelve a hacer dano");
    }

    private static void comprobarMargenesSpectrum() {
        Nivel nivel = crearNivel();
        Spectrum spectrum = agregarSpectrum(nivel, 300);
        Rectangle2D cuerpo = spectrum.obtenerLimitesImpacto();
        Proyectil bala = new Proyectil(300, cuerpo.getCenterY(), 1, 0, 0, 1, Proyectil.Origen.JUGADOR);
        nivel.agregarProyectil(bala);
        GestorColisiones gestor = new GestorColisiones();
        comprobar(bala.obtenerLimites().intersects(spectrum.obtenerLimites()), "Caso dentro de la celda");
        comprobar(gestor.detectarImpactos(nivel).isEmpty(), "El margen izquierdo no recibe dano");
        bala.establecerPosicion(cuerpo.getMaxX(), cuerpo.getCenterY());
        comprobar(gestor.detectarImpactos(nivel).isEmpty(), "El margen derecho no recibe dano");
        bala.establecerPosicion(cuerpo.getCenterX(), cuerpo.getCenterY());
        comprobar(gestor.detectarImpactos(nivel).size() == 1, "El cuerpo si recibe dano");
    }

    private static void comprobarMargenesIan(int direccion) {
        Nivel nivel = crearNivel();
        Jugador ian = nivel.getJugador();
        ian.setDireccion(direccion);
        Rectangle2D cuerpo = ian.obtenerLimitesImpacto();
        double margenX = direccion == 1 ? ian.getX() + ian.getAncho() - 8 : ian.getX();
        Proyectil bala = new Proyectil(margenX, cuerpo.getCenterY(), 0, 1, 0, 1, Proyectil.Origen.ENEMIGO);
        nivel.agregarProyectil(bala);
        GestorColisiones gestor = new GestorColisiones();
        comprobar(gestor.detectarImpactos(nivel).isEmpty(), "El margen de Ian no recibe dano");
        bala.establecerPosicion(cuerpo.getCenterX(), cuerpo.getCenterY());
        comprobar(gestor.detectarImpactos(nivel).size() == 1, "El cuerpo de Ian si recibe dano");
    }

    private static void comprobarPiso() {
        Nivel nivel = new Nivel();
        nivel.cargar();
        nivel.getEnemigos().clear();
        Plataforma piso = nivel.getPlataformas().get(0);
        Jugador ian = nivel.getJugador();
        ian.establecerPosicion(piso.getX() + 20, piso.getY() + 1 - ian.getAlto());
        GestorColisiones gestor = new GestorColisiones();
        gestor.validarMundo(nivel);
        comprobar(ian.getY() + ian.getAlto() == piso.getY(), "El suelo sigue usando la caja fisica completa");
        Proyectil bala = new Proyectil(piso.getX() + 10, piso.getY() + 1, 0, 1, 0, 1, Proyectil.Origen.ENEMIGO);
        nivel.agregarProyectil(bala);
        comprobar(gestor.detectarImpactos(nivel).isEmpty() && !bala.estaActivo(),
                "Las plataformas siguen destruyendo los proyectiles");
    }

    private static void comprobar(boolean condicion, String mensaje) {
        if (!condicion) throw new AssertionError(mensaje);
    }
}
