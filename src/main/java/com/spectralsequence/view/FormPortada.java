package com.spectralsequence.view;

import javax.swing.*;

public class FormPortada {
    private JPanel panelPrincipal;
    private JButton botonNueva;
    private JButton botonRanking;
    private JButton botonInstrucciones;
    private JButton botonSalir;

    public FormPortada(){

        panelPrincipal.setOpaque(false);

        botonNueva.setBorderPainted(false);
        botonNueva.setContentAreaFilled(false);
        botonNueva.setFocusPainted(false);

        botonInstrucciones.setBorderPainted(false);
        botonInstrucciones.setContentAreaFilled(false);
        botonInstrucciones.setFocusPainted(false);

        botonRanking.setBorderPainted(false);
        botonRanking.setContentAreaFilled(false);
        botonRanking.setFocusPainted(false);

        botonSalir.setBorderPainted(false);
        botonSalir.setContentAreaFilled(false);
        botonSalir.setFocusPainted(false);


    }

    public JPanel getPanelPrincipal(){

        return panelPrincipal;


    }

    public JButton getBotonNueva(){

        return botonNueva;
    }

    public JButton getBotonInstrucciones(){

        return botonInstrucciones;

    }

    public JButton getBotonSalir(){

        return botonSalir;


    }

    public JButton getBotonRanking(){

        return botonRanking;
    }












}



