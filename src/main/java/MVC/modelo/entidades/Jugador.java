package MVC.modelo.entidades;

public class Jugador extends Personaje {
    private static final int SALUD_BASE = 7;
    private static final double VELOCIDAD_BASE = 220.0;
    private static final double SALTO = -460;
    private static final double CAIDA = 980.0;
    private static final int DANIO_BASE = 5;
    private static final int CARGA_PODER = 7;
    private static final double TIEMPO_PODER = 8.0;
    private static final double CLDWN_INVULNERABILIDAD = 1.5;

    private double velocidadY;
    private boolean enSuelo;
    private int danioBase;
    private int cargarPoder;
    private boolean poderActivo;
    private double segundosPoder;
    private double invulnerabilidadRestante;

    public Jugador(double x, double y, int ancho, int alto, int saludMaxima, double velocidad) {
        super(x, y, ancho, alto, SALUD_BASE, VELOCIDAD_BASE);
        this.velocidadY = 0.0;
        this.enSuelo = false;
        this.danioBase = DANIO_BASE;
        this.cargarPoder = 0;
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
        velocidadY += CAIDA * dt;
        double nuevaY = getY() + (velocidadY * dt);
        establecerPosicion(getX(), nuevaY);

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
        this.enSuelo = true;
    }

    public void ajustarHorizontal(double xPermitida) {
        establecerPosicion(xPermitida, getY());
    }


    @Override
    public void morir() {

    }
}
