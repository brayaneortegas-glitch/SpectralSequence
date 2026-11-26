package MVC.vista;

import javax.swing.JPanel;

public abstract class PanelResultado extends JPanel {
    public abstract void mostrarResultado(String nombre, int puntaje, int segundos);
    public abstract void mostrarFilaClasificacion(int fila, String nombre, int puntaje, int segundos);
    public abstract void limpiarFilaClasificacion(int fila);
}
