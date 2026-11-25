package com.spectralsequence.view;

import javax.swing.*;

public class FormGameOver {
    private JPanel panelPrincipal;
    private JPanel panelDatos;
    private JLabel valorJugador;
    private JLabel valorResultado;
    private JLabel valorPuntaje;
    private JLabel valorTiempo;
    private JPanel panelTop;
    private JLabel nombre1;
    private JLabel nombre2;
    private JLabel nombre3;
    private JLabel puntos1;
    private JLabel puntos2;
    private JLabel puntos3;
    private JLabel tiempo1;
    private JLabel tiempo2;
    private JLabel tiempo3;
    private JPanel panelBoton;
    private JButton botonVolverInicio;


    public FormGameOver(){

        panelPrincipal.setOpaque(false);

        panelBoton.setOpaque(false);

        panelDatos.setOpaque(false);

        panelTop.setOpaque(false);

        botonVolverInicio.setBorderPainted(false);
        botonVolverInicio.setContentAreaFilled(false);
        botonVolverInicio.setFocusPainted(false);

    }

    public JPanel getPanelPrincipal() {
        return panelPrincipal;
    }

    public JButton getBotonVolverInicio() {
        return botonVolverInicio;
    }
}
