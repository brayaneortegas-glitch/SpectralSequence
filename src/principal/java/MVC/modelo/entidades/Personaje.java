package MVC.modelo.entidades;

import java.awt.geom.Rectangle2D;

public abstract class Personaje extends Entidad {
    private int saludActual;
    private int saludMaxima;
    private double velocidad;
    private int direccion;

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

    // El area que recibe disparos puede ser menor que la celda del sprite.
    public Rectangle2D obtenerLimitesImpacto() {
        return obtenerLimites();
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

    public int getSalud() {
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
