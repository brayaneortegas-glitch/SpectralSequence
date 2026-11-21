package com.spectralsequence.view;

import javax.swing.*;
import java.awt.*;

public class PanelPortada extends JPanel {

    private Image[] fondos = new Image[4];
    private int indice = 0;
    private  Timer timer;

    public PanelPortada(VentanaPrincipal ventana){

        for (int i = 0; i < 4; i++){
            fondos[i]=new ImageIcon(getClass().getResource("/img/menu/PORTADA/Portada_Oscura_fondo_" + (i + 1) + ".png")).getImage();
        }

        timer = new Timer(160, e -> {

            indice = (indice + 1) % fondos.length;

            repaint();

        });

        timer.start();

        setPreferredSize(new Dimension(800,600));
        setLayout(null);

        JButton botonNueva = new JButton();
        botonNueva.addActionListener(e -> ventana.mostrar(new PanelNombre(ventana)));
        botonNueva.setBounds(32,380,240,40);
        botonNueva.setBorderPainted(false);
        botonNueva.setContentAreaFilled(false);
        botonNueva.setFocusPainted(false);

        botonNueva.setIcon(new ImageIcon(getClass().getResource("/img/menu/PORTADA/botones/btn_NUEVA_PARTIDA_normal.png")));
        add(botonNueva);

        JButton botonInstrucciones = new JButton();
        botonInstrucciones.setBounds(32,428,240,40);
        botonInstrucciones.setBorderPainted(false);
        botonInstrucciones.setContentAreaFilled(false);
        botonInstrucciones.setFocusPainted(false);
        botonInstrucciones.setIcon(new ImageIcon(getClass().getResource("/img/menu/PORTADA/botones/btn_INSTRUCCIONES_normal.png")));
        botonInstrucciones.addActionListener(e -> ventana.mostrar(new PanelInstrucciones(ventana)));
        add(botonInstrucciones);

        JButton botonRanking = new JButton();
        botonRanking.setBounds(528,380,240,40);
        botonRanking.setBorderPainted(false);
        botonRanking.setContentAreaFilled(false);
        botonRanking.setFocusPainted(false);
        botonRanking.setIcon(new ImageIcon(getClass().getResource("/img/menu/PORTADA/botones/btn_RANKING_normal.png")));
        add(botonRanking);

        JButton botonSalir = new JButton();
        botonSalir.setBorderPainted(false);
        botonSalir.setContentAreaFilled(false);
        botonSalir.setBounds(528,428,240,40);
        botonSalir.setFocusPainted(false);
        botonSalir.setIcon(new ImageIcon(getClass().getResource("/img/menu/PORTADA/botones/btn_SALIR_normal.png")));
        botonSalir.addActionListener(e -> System.exit(0));
        add(botonSalir);





    }

    @Override
    protected void paintComponent(Graphics g){

        super.paintComponent(g);
        g.drawImage(fondos[indice],0,0,this);


    }


}
