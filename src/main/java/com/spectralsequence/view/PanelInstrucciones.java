package com.spectralsequence.view;

import javax.swing.*;
import java.awt.*;

public class PanelInstrucciones extends JPanel {

    private Image fondo;

    public PanelInstrucciones(VentanaPrincipal ventana){

        fondo = new ImageIcon(getClass().getResource("/img/menu/INSTRUCCIONES/Instrucciones_p1_fondo_1.png")).getImage();

        setPreferredSize(new Dimension(800,600));
        setLayout(null);

        JButton botonVolver = new JButton();
        botonVolver.setBounds(80,524,200,40);
        botonVolver.setBorderPainted(false);
        botonVolver.setContentAreaFilled(false);
        botonVolver.setFocusPainted(false);
        botonVolver.setIcon(new ImageIcon(getClass().getResource("/img/menu/INSTRUCCIONES/botones/btn_VOLVER_normal.png")));
        botonVolver.addActionListener(e -> ventana.mostrar(new PanelPortada(ventana)));
        add(botonVolver);



    }

    @Override
    protected void paintComponent(Graphics g){
        super.paintComponent(g);
        g.drawImage(fondo,0,0,this);
    }








}
