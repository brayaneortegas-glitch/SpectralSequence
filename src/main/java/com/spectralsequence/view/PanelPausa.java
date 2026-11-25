package com.spectralsequence.view;

import javax.swing.*;
import java.awt.*;

public class PanelPausa extends JPanel {

    private Image capa;

    public PanelPausa(VentanaPrincipal ventana){

        capa = new ImageIcon(getClass().getResource("/img/menu/PAUSA/Pausa_capa.png")).getImage();

        setPreferredSize(new Dimension(800,600));
        setLayout(null);

        FormPausa Formulario = new FormPausa();
        JPanel panelPrincipal = Formulario.getPanelPrincipal();
        panelPrincipal.setBounds(232,263,336,160);
        add(panelPrincipal);

        Formulario.getBotonVolverInicio().addActionListener(e -> ventana.mostrar(new PanelPortada(ventana)));
        Formulario.getBotonInstrucciones().addActionListener(e -> ventana.mostrar(new PanelInstrucciones(ventana)));




    }

    @Override
    protected void paintComponent(Graphics g){
        super.paintComponent(g);
        g.drawImage(capa,0,0,this);


    }












}
