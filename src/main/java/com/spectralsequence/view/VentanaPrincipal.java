package com.spectralsequence.view;

import javax.swing.*;

public class VentanaPrincipal extends JFrame {


    public VentanaPrincipal(){

        setTitle("Spectral Sequence");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        add(new PanelPortada(this));
        pack();
        setLocationRelativeTo(null);

    }

    public void mostrar(JPanel panel){

        setContentPane(panel);
        pack();
        revalidate();
        repaint();
    }


}
