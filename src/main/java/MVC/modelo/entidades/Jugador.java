package MVC.modelo.entidades;

public class Jugador extends Personaje {
    private static final int SALUD_BASE = 7;
    private static final double VELOCIDAD_BASE = 220.0;
    private static final double SALTO = -460;
    private static final double CAIDA_G = 980.0;
    private static final int DANIO_BASE = 5;
    private static final int CARGA_MAX = 7;
    private static final double TIEMPO_PODER = 5.0;
    private static final double COOLDOWN_INVULNERABILIDAD = 1.5;
    private static final double ALTURA_PISTOLA = 0.45;
    private static final double VELOCIDAD_PROYECTIL = 500;

    private double velocidadY;
    private boolean enSuelo;
    private int danioBase;
    private int cargaPoder;
    private boolean poderActivo;
    private double segundosPoder;
    private double invulnerabilidadRestante;

    public Jugador(double x, double y, int ancho, int alto) {
        super(x, y, ancho, alto, SALUD_BASE, VELOCIDAD_BASE);
        this.velocidadY = 0.0;
        this.enSuelo = false;
        this.danioBase = DANIO_BASE;
        this.cargaPoder = 0;
        this.poderActivo = false;
        this.segundosPoder = 0.0;
        this.invulnerabilidadRestante = 0.0;

    }

    public void mover(int direccion, double dt) {
        if (direccion == -1 || direccion == 1) {
            setDireccion(direccion);
            double nuevaX = getX() + (direccion * getVelocidad() * dt);
            establecerPosicion(nuevaX, getY());
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
        double nuevaY = getY() + (velocidadY * dt);
        establecerPosicion(getX(), nuevaY);
        enSuelo = false;

        if (poderActivo) {
            segundosPoder -= dt;
            if (segundosPoder <= 0.0) {
                segundosPoder = 0.0;
                poderActivo = false;
            }
        }

        if (invulnerabilidadRestante > 0.0) {
            invulnerabilidadRestante -= dt;
            if (invulnerabilidadRestante < 0.0) {
                invulnerabilidadRestante = 0.0;
            }
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
        double origenX;
        if (getDireccion() == 1) {
            origenX = getX() + getAncho();
        } else {
            origenX = getX();
        }
        double origenY = getY() + (getAlto() * ALTURA_PISTOLA);
        return new Proyectil(origenX, origenY, getDireccion(), 0.0, VELOCIDAD_PROYECTIL, getDanioActual(), Proyectil.Origen.JUGADOR);
    }

    public int getDanioActual() {
        if (poderActivo) {
            return danioBase * 2;
        } else {
            return danioBase;
        }
    }

    @Override
    public boolean recibirDanio(int cantidad) {
        if (cantidad <= 0) {
            return false;
        } else {
            if (estaMuerto() || poderActivo || invulnerabilidadRestante > 0.0) {
                return false;
            }

            boolean daniado = super.recibirDanio(1);
            if (daniado) {
                invulnerabilidadRestante = COOLDOWN_INVULNERABILIDAD;
            }
            return daniado;
        }

    }

    public void registrarImpactoValido() {
        if ((!poderActivo && cargaPoder < CARGA_MAX)) {
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
