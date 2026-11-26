package MVC.modelo.mundo;

import MVC.modelo.entidades.Entidad;

import java.awt.geom.Point2D;

public class PuntoControl extends Entidad {
    private final int numero;
    private final double xReaparicion;
    private final double yReaparicion;


    public PuntoControl(double x, double y, int ancho, int alto, int numero,
                        double xReaparicion, double yReaparicion) {
        super(x, y, ancho, alto);
        if (numero < 0 || numero > 2) {
            throw new IllegalArgumentException(
                    "Rango de checpoints entre 0-2"
            );
        }
        this.numero = numero;
        this.xReaparicion = xReaparicion;
        this.yReaparicion = yReaparicion;
    }

    public int getNumero() {
        return numero;
    }

    public Point2D.Double obtenerPosicionReaparicion() {
        return new Point2D.Double(xReaparicion, yReaparicion);
    }
}
