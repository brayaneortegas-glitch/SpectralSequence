package MVC.vista;

import javax.swing.*;
import java.awt.*;

public class PanelDerrota extends PanelResultado {

    private Image[] fondos = new Image[4];
    private int indice = 0;
    private Timer timer;
    private FormularioDerrota formulario;

    public PanelDerrota(VentanaPrincipal ventana) {

        for (int i = 0; i < 4; i++) {
            fondos[i] = new ImageIcon(getClass().getResource("/imagenes/menu/FIN_PARTIDA/Fin_partida_fondo_" + (i + 1) + ".png")).getImage();
        }


        timer = new Timer(160, e -> {

            indice = (indice + 1) % fondos.length;

            repaint();

        });

        setPreferredSize(new Dimension(800, 600));
        setLayout(null);

        formulario = new FormularioDerrota();
        JPanel panelFormulario = formulario.getPanelPrincipal();
        panelFormulario.setBounds(244, 204, 338, 278);
        add(panelFormulario);

        formulario.getBotonVolverInicio().addActionListener(e -> ventana.mostrar(new PanelPortada(ventana)));

    }

    @Override
    public void addNotify() {
        super.addNotify();
        timer.start();
    }

    @Override
    public void removeNotify() {
        timer.stop();
        super.removeNotify();
    }

    @Override
    public void mostrarResultado(String nombre, int puntaje, int segundos) {
        String puntajeTexto = String.format("%05d", puntaje);
        String tiempoTexto = String.format("%02d:%02d", segundos/60, segundos % 60);

        formulario.setDatos(nombre, "DERROTA", puntajeTexto, tiempoTexto);


    }

    @Override
    public void mostrarFilaClasificacion(int fila, String nombre, int puntaje, int segundos){

        String puntajeTexto = String.format("%05d", puntaje);
        String tiempoTexto = String.format("%02d:%02d", segundos/60, segundos % 60);

        formulario.ponerFilaClasificacion(fila,nombre,puntajeTexto,tiempoTexto);

    }

    @Override
    public void limpiarFilaClasificacion(int fila){

        formulario.ponerFilaClasificacion(fila,"","","");

    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(fondos[indice], 0, 0, this);


    }


}
