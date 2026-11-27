package MVC.modelo.entidades;

import MVC.modelo.mundo.Nivel;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

public class Spectrum extends Enemigo {
    public enum Estado {NORMAL, PENDIENTE, RENACIDO, ELIMINADO}

    private static final double MULTIPLICADOR_VELOCIDAD = 1.4;
    private static final double TIEMPO_PARA_RENACER = 1.5;
    private static final int SALUD_BASE = 12;
    // Cuerpo aproximado dentro de cada celda de 48 x 48, sin los margenes y adornos.
    private static final double IMPACTO_X = 12.0 / 48;
    private static final double IMPACTO_Y = 2.0 / 48;
    private static final double IMPACTO_ANCHO = 20.0 / 48;
    private static final double IMPACTO_ALTO = 42.0 / 48;

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
        this.xOrigen = x;
        this.yOrigen = y;
    }

    public Estado getEstado() {
        return estado;
    }

    @Override
    public Rectangle2D obtenerLimitesImpacto() {
        return new Rectangle2D.Double(getX() + getAncho() * IMPACTO_X,
                getY() + getAlto() * IMPACTO_Y,
                getAncho() * IMPACTO_ANCHO, getAlto() * IMPACTO_ALTO);
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
        if (!detectar(jugador)) {
            return;
        }
        double diferenciaX = (jugador.getX() + jugador.getAncho() / 2.0) - (getX() + getAncho() / 2.0);
        if (diferenciaX == 0.0) {
            return;
        }

        setDireccion(diferenciaX > 0 ? 1 : -1);

        double separacionX = Math.abs(diferenciaX) - (getAncho() + jugador.getAncho()) / 2.0;
        double distanciaRestante = Math.max(0.0, separacionX - rangoAtaque);
        double rapidez = (estado == Estado.RENACIDO) ? velocidadRenacido : getVelocidad();

        double paso = Math.min(rapidez * dt, distanciaRestante);
        establecerPosicion(getX() + getDireccion() * paso, getY());
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
        double dx = 0.0;
        double dy = 0.0;

        if (getX() + getAncho() < jugador.getX()) {
            dx = jugador.getX() - (getX() + getAncho());
        } else if (jugador.getX() + jugador.getAncho() < getX()) {
            dx = getX() - (jugador.getX() + jugador.getAncho());
        }

        if (getY() + getAlto() < jugador.getY()) {
            dy = jugador.getY() - (getY() + getAlto());
        } else if (jugador.getY() + jugador.getAlto() < getY()) {
            dy = getY() - (jugador.getY() + jugador.getAlto());
        }

        return Math.hypot(dx, dy);
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
        if (getY() < jugador.getY() + jugador.getAlto()
                && getY() + getAlto() > jugador.getY()
                && calcularDistancia(jugador) <= rangoAtaque) {
            jugador.recibirDanio(getDanio());
            reiniciarCooldown();
        }
    }

    @Override
    public void actualizar(double dt, Jugador jugador, Nivel nivel) {
        if (estado == Estado.ELIMINADO) {
            return;
        }
        if (estado == Estado.PENDIENTE) {
            segundosParaRenacer -= dt;
            if (segundosParaRenacer <= 0.0 && jugador != null && jugador.estaVivo()) {
                Point2D.Double pos = nivel.buscarPosicionSegura(jugador);
                if (pos != null) {
                    renacer(pos.x, pos.y);
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
