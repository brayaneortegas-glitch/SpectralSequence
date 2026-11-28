package MVC.vista;


import javax.swing.*;
import java.awt.*;

public class Indicadores {

    private Image [] vida = new Image[8];
    private Image [] poder = new Image[8];
    private Image [] vidas = new  Image[4];



    public Indicadores(){

        for (int i = 0; i < 8; i++){

            vida[i] = new ImageIcon(getClass().getResource("/imagenes/menu/INDICADORES/vida_" + i + "_x2.png")).getImage();


        }

        for (int i = 0; i < 8; i++){

            poder[i] = new ImageIcon(getClass().getResource("/imagenes/menu/INDICADORES/poder_" + i + "_x2.png")).getImage();

        }


        for (int i = 0; i < 4; i++){

            vidas[i] = new  ImageIcon(getClass().getResource("/imagenes/menu/INDICADORES/vidas_" + i + "_x2.png")).getImage();

        }

    }

    public void dibujar(Graphics g,int salud, int nivelpoder, int vidasRestantes){


        g.drawImage(vida[salud], 8,6,null);
        g.drawImage(poder[nivelpoder],8,36,null);
        g.drawImage(vidas[vidasRestantes],176,8,null);

    }

    public void dibujarTexto(Graphics g, int puntos, int segundos){

        String textoPuntos = String.format("%05d", puntos);
        String textoTiempo = String.format("%02d:%02d", segundos /60, segundos % 60);

        g.setColor(Color.WHITE);
        g.setFont(new Font("Monospaced", Font.BOLD, 16));
        g.drawString(textoPuntos, 700, 24);
        g.drawString(textoTiempo, 700, 44);






    }





}
