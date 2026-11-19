package com.spectralsequence.view;

import javax.swing.*;

public class VentanaPrincipal extends JFrame {


    public VentanaPrincipal(){

        setTitle("Spectral Sequence");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        add(new PanelPortada());
        pack();
        setLocationRelativeTo(null);

    }


}
