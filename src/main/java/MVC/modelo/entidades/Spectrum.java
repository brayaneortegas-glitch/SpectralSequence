package MVC.modelo.entidades;

import MVC.modelo.mundo.Nivel;

import java.awt.geom.Point2D;

public class Spectrum extends Enemigo {
    public enum Estado {NORMAL, PENDIENTE, RENACIDO, ELIMINADO}

    private static final double MULTIPLICADOR_VELOCIDAD = 1.4;
    private static final double TIEMPO_PARA_RENACER = 1.5;
    private static final int SALUD_BASE = 12;
    private Estado estado = Estado.NORMAL;
    private final double rangoDeteccion;
    private final double rangoAtaque;
    private final double velocidadRenacido;
    private double segundosParaRenacer;
    private final double xOrigen;
    private final double yOrigen;


    public Spectrum(int id, int danio, double cooldownAtaque, double x, double y,
                    int ancho, int alto, double velocidad, double rangoDeteccion,
                    double rangoAtaque) {
        super(id, danio, cooldownAtaque, x, y, ancho, alto, SALUD_BASE, velocidad);
        this.rangoDeteccion = rangoDeteccion;
        this.velocidadRenacido = velocidad * MULTIPLICADOR_VELOCIDAD;
        this.rangoAtaque = rangoAtaque;
        this.segundosParaRenacer = 0.0;
        this.xOrigen = x;
        this.yOrigen = y;
    }


    public Estado getEstado() {
        return estado;
    }

    public boolean estaDisponible() {
        return estado == Estado.NORMAL || estado == Estado.RENACIDO;
    }

    public boolean detectar(Jugador jugador) {
        if (jugador == null || jugador.estaMuerto() || !estaDisponible()) {
            return false;
        }
        return calcularDistancia(jugador) <= rangoDeteccion;
    }

    public void perseguir(Jugador jugador, double dt) {
        if (dt <= 0.0 || !detectar(jugador)) {
            return;
        }
        double centroSpectrum = getX() + getAncho() / 2.0;
        double centroJugador = jugador.getX() + jugador.getAncho() / 2.0;
        double diferenciaX = centroJugador - centroSpectrum;

        if (diferenciaX == 0.0) {
            return;
        }
        if (diferenciaX > 0.0) {
            setDireccion(1);
        } else {
            setDireccion(-1);
        }

        double separacionX = Math.abs(diferenciaX) - (getAncho() + jugador.getAncho()) / 2.0;
        double distanciaPorRecorrer = Math.max(0.0, separacionX - rangoAtaque);
        double rapidez;

        if (estado == Estado.RENACIDO) {
            rapidez = velocidadRenacido;
        } else {
            rapidez = getVelocidad();
        }

        double desplazamiento = Math.min(rapidez * dt, distanciaPorRecorrer);
        double nuevaX = getX() + getDireccion() * desplazamiento;

        establecerPosicion(nuevaX, getY());
    }

    public void renacer(double x, double y) {
        if (estado != Estado.PENDIENTE || segundosParaRenacer > 0.0) {
            return;
        }
        establecerPosicion(x, y);
        restaurarSalud();
        segundosParaRenacer = 0.0;
        estado = Estado.RENACIDO;
    }

    public double calcularDistancia(Jugador jugador) {
        double separacionX = 0.0;
        double separacionY = 0.0;
        if (getX() + getAncho() < jugador.getX()) {
            separacionX = jugador.getX() - (getX() - getAncho());
        } else if (jugador.getX() + jugador.getAncho() < jugador.getX()) {
            separacionY = getX() - (jugador.getX() - jugador.getAncho());
        }
        if (getY() + getAlto() < jugador.getY()) {
            separacionY = jugador.getY() - (getY() - getAlto());
        } else if (jugador.getY() + jugador.getAlto() < jugador.getY()) {
            separacionY = getY() - (jugador.getY() - jugador.getAlto());
        }
        return Math.hypot(separacionX, separacionY);
    }

    public void restablecerTrasMuerteIan() {
        if (estado != Estado.ELIMINADO) {
            establecerPosicion(xOrigen, yOrigen);
        }
    }


    @Override
    public boolean recibirDanio(int cantidad) {
        if (!estaDisponible()) {
            return false;
        }
        return super.recibirDanio(cantidad);
    }

    @Override
    public void atacar(Jugador jugador, Nivel nivel) {
        if (!estaDisponible() || !puedeAtacar() || jugador == null || jugador.estaMuerto()) {
            return;
        }
        boolean coincideAltura = getY() < jugador.getY() + jugador.getAlto()
                && getY() + getAlto() > jugador.getY();
        if (coincideAltura && calcularDistancia(jugador) <= rangoAtaque) {
            jugador.recibirDanio(getDanio());
            reiniciarCooldown();
        }
    }

    @Override
    public void actualizar(double dt, Jugador jugador, Nivel nivel) {
        if (dt <= 0.0 || estado == Estado.ELIMINADO) {
            return;
        }
        if (estado == Estado.PENDIENTE) {
            segundosParaRenacer -= dt;
            if (segundosParaRenacer <= 0.0 && jugador != null && jugador.estaVivo()) {
                Point2D.Double posicion = nivel.buscarPosicionSegura(jugador);
                if (posicion != null) {
                    renacer(posicion.x, posicion.y);
                }
            }
            return;
        }
        actualizarCooldown(dt);
        if (detectar(jugador)) {
            perseguir(jugador, dt);
            atacar(jugador, nivel);
        }
    }

    @Override
    public void morir() {
        if (estaVivo()) {
            return;
        }
        if (estado == Estado.NORMAL) {
            estado = Estado.PENDIENTE;
            segundosParaRenacer = TIEMPO_PARA_RENACER;
        } else if (estado == Estado.RENACIDO) {
            estado = Estado.ELIMINADO;
        }
    }

}
