package MVC.vista;

import javax.swing.*;
import java.awt.*;

public class PanelJuego extends JPanel {

    private Image cielo;
    private Image catedral;
    private Image ruinas;
    private int camaraX = 400;
    private  final int ESCALA = 2;
    private Timer timer;


    public PanelJuego(){

        cielo = new ImageIcon(getClass().getResource("/imagenes/nivel/fondo/Fondo_capa_cielo.png")).getImage();
        catedral = new  ImageIcon(getClass().getResource("/imagenes/nivel/fondo/Fondo_capa_catedral.png")).getImage();
        ruinas  = new  ImageIcon(getClass().getResource("/imagenes/nivel/fondo/Fondo_capa_ruinas.png")).getImage();




        setPreferredSize(new Dimension(800, 600));
        setLayout(null);

        timer = new Timer(16, e -> {

            camaraX +=2;

            if (camaraX > 1600){

                camaraX = 0;

            }

            repaint();

        });

        timer.start();







    }

    @Override
    protected  void paintComponent(Graphics g){
        super.paintComponent(g);

        dibujarCapa(g,cielo, 0.1);
        dibujarCapa(g, catedral, 0.3);
        dibujarCapa(g, ruinas, 0.6);

    }

    private void dibujarCapa(Graphics g, Image imagen, double velocidad){

        double desplazamiento = -(camaraX * velocidad) % 800;
        int x = (int) (desplazamiento * ESCALA);
        g.drawImage(imagen,x, 0, 1600, 600, this);
        g.drawImage(imagen, x + 1600, 0, 1600, 600, this);





    }











}
