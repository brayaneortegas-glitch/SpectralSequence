package com.spectralsequence.view;

import javax.swing.*;
import java.awt.*;

public class PanelNombre extends JPanel {

    private Image fondo;

    public PanelNombre(VentanaPrincipal ventana){

        fondo = new ImageIcon(getClass().getResource("/img/menu/NOMBRE/Pantalla_Nombre_fondo_1.png")).getImage();

        setPreferredSize(new Dimension(800,600));
        setLayout(null);

        FormNombre formulario = new FormNombre();
        JPanel panelFormulario = formulario.getPanelPrincipal();
        panelFormulario.setBounds(204,264,392,204);
        add(panelFormulario);






    }

    @Override
    protected void paintComponent(Graphics g){

        super.paintComponent(g);
        g.drawImage(fondo, 0,0,this);



    }






}
