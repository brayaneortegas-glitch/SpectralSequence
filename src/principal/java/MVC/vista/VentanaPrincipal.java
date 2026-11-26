package MVC.vista;

import MVC.controlador.ControladorJuego;

import javax.swing.*;
import java.io.IOException;

public class VentanaPrincipal extends JFrame {


    public VentanaPrincipal() {

        setTitle("Spectral Sequence");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        add(new PanelPortada(this));
        pack();
        setLocationRelativeTo(null);

    }

    public void mostrarFinPartida(ControladorJuego controlador) throws IOException {
        mostrar(controlador.crearPanelResultado(this));
    }

    public void mostrar(JPanel panel) {

        setContentPane(panel);
        pack();
        revalidate();
        repaint();
    }


}
