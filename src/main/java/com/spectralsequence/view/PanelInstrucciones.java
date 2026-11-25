package com.spectralsequence.view;

import javax.swing.*;
import java.awt.*;

public class PanelInstrucciones extends JPanel {

    private Image[] fondos = new Image[2];
    private int pagina = 0;


    public PanelInstrucciones(VentanaPrincipal ventana){

        fondos[0]= new ImageIcon(getClass().getResource("/img/menu/INSTRUCCIONES/Instrucciones_p1_fondo_1.png")).getImage();
        fondos[1]= new ImageIcon(getClass().getResource("/img/menu/INSTRUCCIONES/Instrucciones_p2_fondo_1.png")).getImage();

        setPreferredSize(new Dimension(800,600));
        setLayout(null);

       FormInstrucciones formulario = new FormInstrucciones();
       JPanel panelFormulario = formulario.getPanelPrincipal();
       panelFormulario.setBounds(80,524,640,40);
       add(panelFormulario);

       formulario.getBotonAnterior().setVisible(false);

       formulario.getBotonVolver().addActionListener(e -> ventana.mostrar(new PanelPortada(ventana)));

       formulario.getBotonSiguiente().addActionListener(e -> {

           pagina = 1;
           formulario.getBotonSiguiente().setVisible(false);
           formulario.getBotonAnterior().setVisible(true);
           repaint();
       });

       formulario.getBotonAnterior().addActionListener(e -> {

           pagina = 0;
           formulario.getBotonSiguiente().setVisible(true);
           formulario.getBotonAnterior().setVisible(false);
           repaint();
       });




    }

    @Override
    protected void paintComponent(Graphics g){
        super.paintComponent(g);
        g.drawImage(fondos[pagina],0,0,this);
    }








}
