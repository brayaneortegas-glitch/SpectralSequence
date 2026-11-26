package MVC.view;

import javax.swing.*;

public class FormPausa {
    private JPanel panelPrincipal;
    private JButton botonContinuar;
    private JButton botonCheckpoint;
    private JButton botonReiniciar;
    private JButton botonInstrucciones;
    private JButton botonVolverInicio;


    public FormPausa() {

        panelPrincipal.setOpaque(false);

        botonContinuar.setBorderPainted(false);
        botonContinuar.setContentAreaFilled(false);
        botonContinuar.setFocusPainted(false);

        botonCheckpoint.setBorderPainted(false);
        botonCheckpoint.setContentAreaFilled(false);
        botonCheckpoint.setFocusPainted(false);

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

    public JButton getBotonCheckpoint() {
        return botonCheckpoint;
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
