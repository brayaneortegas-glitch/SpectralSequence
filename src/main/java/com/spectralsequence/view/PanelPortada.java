package com.spectralsequence.view;

import javax.swing.*;
import java.awt.*;

public class PanelPortada extends JPanel {

    private Image[] fondos = new Image[4];
    private int indice = 0;
    private  Timer timer;

    public PanelPortada(){

        for (int i = 0; i < 4; i++){
            fondos[i]=new ImageIcon(getClass().getResource("/img/menu/PORTADA/Portada_Oscura_fondo_" + (i + 1) + ".png")).getImage();
        }

        timer = new Timer(160, e -> {

            indice = (indice + 1) % fondos.length;

            repaint();

        });

        timer.start();


        setPreferredSize(new Dimension(800,600));
    }

    @Override
    protected void paintComponent(Graphics g){
        super.paintComponent(g);
        g.drawImage(fondos[indice],0,0,this);
    }

}
