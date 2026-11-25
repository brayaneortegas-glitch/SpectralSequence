package MVC.modelo.objetos;

import MVC.modelo.entidades.Entidad;

/** Objeto que ocupa un lugar del nivel y puede recogerse una vez. */
public class ObjetoRecogible extends Entidad {
    /** Efectos que el controlador puede aplicar al recoger el objeto. */
    public enum Tipo {
        /** Recupera una vida. */
        VIDA,
        /** Permite terminar el nivel después de derrotar al jefe. */
        NEXUS
    }

    private final int id;
    private final Tipo tipo;
    private boolean recogido;

    /**
     * Coloca un objeto disponible en el nivel.
     * @param x posición horizontal
     * @param y posición vertical
     * @param ancho ancho de la zona de contacto
     * @param alto alto de la zona de contacto
     * @param id identificador del objeto en el nivel
     * @param tipo vida extra o Nexus
     */
    public ObjetoRecogible(double x, double y, int ancho, int alto, int id, Tipo tipo) {
        super(x, y, ancho, alto);
        if (tipo == null) {
            throw new IllegalArgumentException("El objeto necesita un tipo");
        }
        this.id = id;
        this.tipo = tipo;
    }

    /** {@return identificador del objeto} */
    public int getId() {
        return id;
    }

    /** {@return tipo de efecto} */
    public Tipo getTipo() {
        return tipo;
    }

    /** {@return verdadero mientras se pueda recoger} */
    public boolean estaDisponible() {
        return !recogido;
    }

    /** Marca el objeto después de que el controlador haya aplicado su efecto. */
    public void marcarRecogido() {
        recogido = true;
    }
}
