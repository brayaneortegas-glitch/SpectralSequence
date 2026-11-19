package com.spectralsequence.view;

import javax.swing.*;
import java.awt.*;

public class PanelPortada extends JPanel {

    private Image fondo;

    public PanelPortada(){

        fondo = new ImageIcon(getClass().getResource("/img/menu/PORTADA/Portada_Oscura_fondo_1.png")).getImage() ;
        setPreferredSize(new Dimension(800,600));
    }

    @Override
    protected void paintComponent(Graphics g){
        super.paintComponent(g);
        g.drawImage(fondo,0,0,this);
    }

}
