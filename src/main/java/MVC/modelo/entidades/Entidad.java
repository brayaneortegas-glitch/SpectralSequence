package MVC.modelo.entidades;

import java.awt.geom.Rectangle2D;

public abstract class Entidad {
    private double x;
    private double y;
    private int ancho;
    private int alto;

    Entidad(double x, double y, int ancho, int alto) {
        this.x = x;
        this.y = y;
        this.ancho = ancho;
        this.alto = alto;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public int getAlto() {
        return alto;
    }

    public int getAncho() {
        return ancho;
    }

    public Rectangle2D obtenerLimites() {
        return new Rectangle2D.Double(x, y, ancho, alto);
    }

    public void establecerPosicion(double x, double y) {
        this.x = x;
        this.y = y;
    }

}
