package com.spectralsequence.view;

import javax.swing.*;

public class FormInstrucciones {
    private JPanel panelPrincipal;
    private JButton botonVolver;
    private JButton botonAnterior;
    private JButton botonSiguiente;

    public FormInstrucciones(){

        panelPrincipal.setOpaque(false);

        botonVolver.setBorderPainted(false);
        botonVolver.setContentAreaFilled(false);
        botonVolver.setFocusPainted(false);

        botonAnterior.setBorderPainted(false);
        botonAnterior.setContentAreaFilled(false);
        botonAnterior.setFocusPainted(false);

        botonSiguiente.setBorderPainted(false);
        botonSiguiente.setContentAreaFilled(false);
        botonSiguiente.setFocusPainted(false);



    }

    public JPanel getPanelPrincipal(){

        return panelPrincipal;


    }

    public JButton getBotonVolver(){

        return botonVolver;
    }

    public JButton getBotonAnterior(){

        return botonAnterior;

    }

    public JButton getBotonSiguiente(){

        return botonSiguiente;


    }

}
