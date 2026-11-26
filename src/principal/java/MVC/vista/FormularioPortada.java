package MVC.vista;

import javax.swing.*;

public class FormularioPortada {
    private JPanel panelPrincipal;
    private JButton botonNueva;
    private JButton botonClasificacion;
    private JButton botonInstrucciones;
    private JButton botonSalir;

    public FormularioPortada() {

        panelPrincipal.setOpaque(false);

        botonNueva.setBorderPainted(false);
        botonNueva.setContentAreaFilled(false);
        botonNueva.setFocusPainted(false);

        botonInstrucciones.setBorderPainted(false);
        botonInstrucciones.setContentAreaFilled(false);
        botonInstrucciones.setFocusPainted(false);

        botonClasificacion.setBorderPainted(false);
        botonClasificacion.setContentAreaFilled(false);
        botonClasificacion.setFocusPainted(false);

        botonSalir.setBorderPainted(false);
        botonSalir.setContentAreaFilled(false);
        botonSalir.setFocusPainted(false);


    }

    public JPanel getPanelPrincipal() {

        return panelPrincipal;


    }

    public JButton getBotonNueva() {

        return botonNueva;
    }

    public JButton getBotonInstrucciones() {

        return botonInstrucciones;

    }

    public JButton getBotonSalir() {

        return botonSalir;


    }

    public JButton getBotonClasificacion() {

        return botonClasificacion;
    }


}



