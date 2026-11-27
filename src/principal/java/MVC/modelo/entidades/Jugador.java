package MVC.modelo.entidades;

import java.awt.geom.Rectangle2D;

public class Jugador extends Personaje {
    private static final int SALUD_BASE = 7;
    private static final double VELOCIDAD_BASE = 220.0;
    private static final double SALTO = -460;
    private static final double CAIDA_G = 980.0;
    private static final int DANIO_BASE = 1;
    private static final int CARGA_MAX = 7;
    private static final double TIEMPO_PODER = 5.0;
    private static final double COOLDOWN_INVULNERABILIDAD = 1.5;
    private static final double ALTURA_PISTOLA = 0.45;
    private static final double VELOCIDAD_PROYECTIL = 500;
    // Cuerpo aproximado en las celdas de Ian (80 x 72), mirando a la derecha.
    private static final double IMPACTO_X = 14.0 / 80;
    private static final double IMPACTO_Y = 8.0 / 72;
    private static final double IMPACTO_ANCHO = 26.0 / 80;
    private static final double IMPACTO_ALTO = 54.0 / 72;

    private double velocidadY;
    private boolean enSuelo;
    private int danioBase;
    private int cargaPoder;
    private boolean poderActivo;
    private double segundosPoder;
    private double invulnerabilidadRestante;

    public Jugador(double x, double y, int ancho, int alto) {
        super(x, y, ancho, alto, SALUD_BASE, VELOCIDAD_BASE);
        this.danioBase = DANIO_BASE;
    }

    public void mover(int direccion, double dt) {
        if (direccion == -1 || direccion == 1) {
            setDireccion(direccion);
            establecerPosicion(getX() + direccion * getVelocidad() * dt, getY());
        }
    }

    public void saltar() {
        if (enSuelo) {
            velocidadY = SALTO;
            enSuelo = false;
        }
    }

    public void actualizar(double dt) {
        velocidadY += CAIDA_G * dt;
        establecerPosicion(getX(), getY() + velocidadY * dt);
        enSuelo = false;

        if (poderActivo) {
            segundosPoder -= dt;
            if (segundosPoder <= 0.0) {
                segundosPoder = 0.0;
                poderActivo = false;
            }
        }

        if (invulnerabilidadRestante > 0.0) {
            invulnerabilidadRestante = Math.max(0.0, invulnerabilidadRestante - dt);
        }
    }

    public void aterrizarSobre(double ySuperior) {
        establecerPosicion(getX(), ySuperior - getAlto());
        velocidadY = 0.0;
        enSuelo = true;
    }

    public void ajustarHorizontal(double xPermitida) {
        establecerPosicion(xPermitida, getY());
    }

    public Proyectil disparar() {
        Rectangle2D cuerpo = obtenerLimitesImpacto();
        double origenX = (getDireccion() == 1) ? cuerpo.getMaxX() : cuerpo.getMinX();
        double origenY = getY() + getAlto() * ALTURA_PISTOLA;
        Proyectil bala = new Proyectil(origenX, origenY, getDireccion(), 0.0,
                VELOCIDAD_PROYECTIL, getDanioActual(), Proyectil.Origen.JUGADOR);
        // x/y son la esquina de la bala: centrarla en la mano y sacarla hacia fuera.
        bala.establecerPosicion(origenX - (getDireccion() < 0 ? bala.getAncho() : 0),
                origenY - bala.getAlto() / 2.0);
        return bala;
    }

    @Override
    public Rectangle2D obtenerLimitesImpacto() {
        double inicioX = getDireccion() == 1 ? IMPACTO_X : 1.0 - IMPACTO_X - IMPACTO_ANCHO;
        return new Rectangle2D.Double(getX() + getAncho() * inicioX,
                getY() + getAlto() * IMPACTO_Y,
                getAncho() * IMPACTO_ANCHO, getAlto() * IMPACTO_ALTO);
    }

    public int getDanioActual() {
        return poderActivo ? danioBase * 2 : danioBase;
    }

    @Override
    public boolean recibirDanio(int cantidad) {
        if (cantidad <= 0 || estaMuerto() || poderActivo || invulnerabilidadRestante > 0.0) {
            return false;
        }
        super.recibirDanio(cantidad);
        invulnerabilidadRestante = COOLDOWN_INVULNERABILIDAD;
        return true;
    }

    public void registrarImpactoValido() {
        if (!poderActivo && cargaPoder < CARGA_MAX) {
            cargaPoder++;
        }
    }

    public int getCargaPoder() {
        return cargaPoder;
    }

    public boolean activarPoder() {
        if (cargaPoder == CARGA_MAX && !poderActivo) {
            cargaPoder = 0;
            poderActivo = true;
            segundosPoder = TIEMPO_PODER;
            return true;
        }
        return false;
    }

    public boolean isPoderActivo() {
        return poderActivo;
    }

    public void morirPorPinchos() {
        if (!estaMuerto()) {
            poderActivo = false;
            segundosPoder = 0.0;
            invulnerabilidadRestante = 0.0;
            super.recibirDanio(getSalud());
        }
    }

    @Override
    public void morir() {
        velocidadY = 0.0;
        poderActivo = false;
    }

    public boolean estaMuerto() {
        return !estaVivo();
    }

    public void reaparecer(double x, double y) {
        establecerPosicion(x, y);
        restaurarSalud();
        velocidadY = 0.0;
        enSuelo = false;
        poderActivo = false;
        segundosPoder = 0.0;
        cargaPoder = 0;
        invulnerabilidadRestante = 0.0;
    }
}
