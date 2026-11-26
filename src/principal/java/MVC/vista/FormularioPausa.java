package MVC.vista;

import javax.swing.*;

public class FormularioPausa {
    private JPanel panelPrincipal;
    private JButton botonContinuar;
    private JButton botonPuntoControl;
    private JButton botonReiniciar;
    private JButton botonInstrucciones;
    private JButton botonVolverInicio;


    public FormularioPausa() {

        panelPrincipal.setOpaque(false);

        botonContinuar.setBorderPainted(false);
        botonContinuar.setContentAreaFilled(false);
        botonContinuar.setFocusPainted(false);

        botonPuntoControl.setBorderPainted(false);
        botonPuntoControl.setContentAreaFilled(false);
        botonPuntoControl.setFocusPainted(false);

        botonReiniciar.setBorderPainted(false);
        botonReiniciar.setContentAreaFilled(false);
        botonReiniciar.setFocusPainted(false);

        botonInstrucciones.setBorderPainted(false);
        botonInstrucciones.setContentAreaFilled(false);
        botonInstrucciones.setFocusPainted(false);

        botonVolverInicio.setBorderPainted(false);
        botonVolverInicio.setContentAreaFilled(false);
        botonVolverInicio.setFocusPainted(false);

    }

    public JPanel getPanelPrincipal() {
        return panelPrincipal;
    }

    public JButton getBotonPuntoControl() {
        return botonPuntoControl;
    }

    public JButton getBotonInstrucciones() {
        return botonInstrucciones;
    }

    public JButton getBotonReiniciar() {
        return botonReiniciar;
    }

    public JButton getBotonVolverInicio() {
        return botonVolverInicio;
    }

    public JButton getBotonContinuar() {
        return botonContinuar;
    }
}
