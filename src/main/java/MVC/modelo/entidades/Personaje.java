package MVC.modelo.entidades;

abstract class Personaje extends Entidad {
    protected int saludActual;
    protected int saludMaxima;
    protected double velocidad;
    protected int direccion;

    public Personaje(double x, double y, int ancho, int alto, int saludMaxima, double velocidad) {
        super(x, y, ancho, alto);
        this.saludMaxima = saludMaxima;
        this.saludActual = saludMaxima;
        this.velocidad = velocidad;
        this.direccion = 1;
    }

    public boolean estaVivo() {
        return saludActual > 0;
    }

    public boolean recibirDanio(int cantidad) {
        if (!estaVivo() || cantidad <= 0) {
            return false;
        }
        saludActual -= cantidad;

        if (saludActual <= 0) {
            saludActual = 0;
            morir();
        }
        return true;
    }

    public int getSaludActual() {
        return saludActual;
    }

    public int getSaludMaxima() {
        return saludMaxima;
    }

    public void restaurarSalud() {
        saludActual = saludMaxima;
    }

    public double getVelocidad() {
        return velocidad;
    }

    public int getDireccion() {
        return direccion;
    }

    public void setDireccion(int direccion) {
        if (direccion == -1 || direccion == 1) {
            this.direccion = direccion;
        }
    }

    public abstract void morir();
}
