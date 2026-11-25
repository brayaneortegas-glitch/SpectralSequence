package com.spectralsequence.view;

import javax.swing.*;

public class FormRanking {
    private JPanel panelPrincipal;
    private JLabel nombre1;
    private JLabel puntos1;
    private JLabel tiempo1;
    private JLabel nombre2;
    private JLabel puntos2;
    private JLabel tiempo2;
    private JLabel nombre3;
    private JLabel puntos3;
    private JLabel tiempo3;
    private JButton botonVolver;


    public FormRanking(){

        panelPrincipal.setOpaque(false);

        botonVolver.setBorderPainted(false);
        botonVolver.setContentAreaFilled(false);
        botonVolver.setFocusPainted(false);

    }

    public JPanel getPanelPrincipal(){

        return panelPrincipal;


    }

    public JButton getBotonVolver(){

        return botonVolver;
    }









}
