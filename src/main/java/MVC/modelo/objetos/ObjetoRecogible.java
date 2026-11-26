package MVC.modelo.objetos;

import MVC.modelo.entidades.Entidad;

public class ObjetoRecogible extends Entidad {
    public enum Tipo {
        VIDA,
        NEXUS
    }

    private final int id;
    private final Tipo tipo;
    private boolean recogido;

    public ObjetoRecogible(double x, double y, int ancho, int alto, int id, Tipo tipo) {
        super(x, y, ancho, alto);
        if (tipo == null) {
            throw new IllegalArgumentException("El objeto necesita un tipo");
        }
        this.id = id;
        this.tipo = tipo;
    }

    public int getId() {
        return id;
    }

    public Tipo getTipo() {
        return tipo;
    }

    public boolean estaDisponible() {
        return !recogido;
    }

    public void marcarRecogido() {
        recogido = true;
    }
}
