package com.spectralsequence.view;

import javax.swing.*;
import java.awt.*;

public class PanelInstrucciones extends JPanel {

    private Image[] fondos = new Image[2];
    private int pagina = 0;
    private JButton botonVolver;
    private JButton botonAnterior;
    private JButton botonSiguiente;

    public PanelInstrucciones(VentanaPrincipal ventana){

        fondos[0]= new ImageIcon(getClass().getResource("/img/menu/INSTRUCCIONES/Instrucciones_p1_fondo_1.png")).getImage();
        fondos[1]= new ImageIcon(getClass().getResource("/img/menu/INSTRUCCIONES/Instrucciones_p2_fondo_1.png")).getImage();

        setPreferredSize(new Dimension(800,600));
        setLayout(null);

        botonVolver = new JButton();
        botonVolver.setBounds(80,524,200,40);
        botonVolver.setBorderPainted(false);
        botonVolver.setContentAreaFilled(false);
        botonVolver.setFocusPainted(false);
        botonVolver.setIcon(new ImageIcon(getClass().getResource("/img/menu/INSTRUCCIONES/botones/btn_VOLVER_normal.png")));
        botonVolver.addActionListener(e -> ventana.mostrar(new PanelPortada(ventana)));
        add(botonVolver);


        botonSiguiente = new JButton();
        botonSiguiente.setBounds(520,524,200,40);
        botonSiguiente.setBorderPainted(false);
        botonSiguiente.setContentAreaFilled(false);
        botonSiguiente.setFocusPainted(false);
        botonSiguiente.setIcon(new ImageIcon(getClass().getResource("/img/menu/INSTRUCCIONES/botones/btn_SIGUIENTE_normal.png")));
        botonSiguiente.addActionListener(e -> {
            pagina = 1;
            botonVolver.setVisible(false);
            botonSiguiente.setVisible(false);
            botonAnterior.setVisible(true);
            repaint();


        });

        botonAnterior = new JButton();
        botonAnterior.setBounds(80,524,200,40);
        botonAnterior.setBorderPainted(false);
        botonAnterior.setContentAreaFilled(false);
        botonAnterior.setFocusPainted(false);
        botonAnterior.setIcon(new ImageIcon(getClass().getResource("/img/menu/INSTRUCCIONES/botones/btn_ANTERIOR_normal.png")));
        botonAnterior.setVisible(false);
        botonAnterior.addActionListener(e -> {

            pagina = 0;
            botonVolver.setVisible(true);
            botonSiguiente.setVisible(true);
            botonAnterior.setVisible(false);
            repaint();

        });


        add(botonSiguiente);
        add(botonAnterior);





    }

    @Override
    protected void paintComponent(Graphics g){
        super.paintComponent(g);
        g.drawImage(fondos[pagina],0,0,this);
    }








}
