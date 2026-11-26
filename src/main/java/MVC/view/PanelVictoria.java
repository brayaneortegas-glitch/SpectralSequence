package MVC.view;

import javax.swing.*;
import java.awt.*;

public class PanelVictoria extends JPanel {

    private Image[] fondos = new Image[8];
    private int indice = 0;
    private Timer timer;
    private FormVictoria formulario;

    public PanelVictoria(VentanaPrincipal ventana) {


        for (int i = 0; i < 8; i++) {
            fondos[i] = new ImageIcon(getClass().getResource("/img/menu/VICTORIA/Victoria_fondo_" + (i + 1) + ".png")).getImage();
        }

        timer = new Timer(160, e -> {

            indice = (indice + 1) % fondos.length;

            repaint();

        });

        timer.start();

        setPreferredSize(new Dimension(800, 600));
        setLayout(null);

        formulario = new FormVictoria();
        JPanel panelFormulario = formulario.getPanelPrincipal();
        panelFormulario.setBounds(244, 204, 338, 278);
        add(panelFormulario);

        formulario.getBotonVolverInicio().addActionListener(e -> ventana.mostrar(new PanelPortada(ventana)));

    }

    public void mostrarResultado(String nombre, int puntaje, int segundos){

        String puntajeTexto = String.format("%05d",puntaje);
        String tiempoTexto = String.format("%02d:%02d", segundos / 60, segundos % 60);
        formulario.setDatos(nombre,"VICTORIA", puntajeTexto, tiempoTexto);




    }

    public void mostrarFilaTop(int fila, String nombre, int puntaje, int segundos){

        String puntajeTexto = String.format("%05d", puntaje);
        String tiempoTexto = String.format("%02d:%02d", segundos/60, segundos % 60);

        formulario.setFilaTop(fila,nombre,puntajeTexto,tiempoTexto);

    }


    public void limpiarFilaTop(int fila){

        formulario.setFilaTop(fila,"","","");

    }



    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(fondos[indice], 0, 0, this);


    }




}
