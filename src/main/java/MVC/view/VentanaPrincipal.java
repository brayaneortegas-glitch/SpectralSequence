package MVC.view;

import MVC.controlador.GameController;

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

    public void mostrarFinPartida(GameController controlador) throws IOException {
        PanelGameOver panel = new PanelGameOver(this);
        controlador.finalizarPartida(panel);
        mostrar(panel);
    }

    public void mostrar(JPanel panel) {

        setContentPane(panel);
        pack();
        revalidate();
        repaint();
    }


}
