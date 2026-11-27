package MVC.vista;

import MVC.controlador.ControladorJuego;
import MVC.modelo.partida.RegistroPartida;
import java.util.List;

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
        RegistroPartida resultado = controlador.finalizarPartida();
        mostrar(crearPanelResultado(this, resultado, controlador.obtenerTresMejores()));
    }

    public static PanelResultado crearPanelResultado(VentanaPrincipal ventana, RegistroPartida resultado,
                                                    List<RegistroPartida> mejores) {
        PanelResultado panel;
        if (resultado.victoria()) {
            panel = new PanelVictoria(ventana);
        } else {
            panel = new PanelDerrota(ventana);
        }
        panel.mostrarResultado(resultado.nombre(), resultado.puntaje(), resultado.segundos());

        for (int fila = 1; fila <= 3; fila++) {
            if (fila <= mejores.size()) {
                RegistroPartida registro = mejores.get(fila - 1);
                panel.mostrarFilaClasificacion(fila, registro.nombre(), registro.puntaje(), registro.segundos());
            } else {
                panel.limpiarFilaClasificacion(fila);
            }
        }
        return panel;
    }

    public void mostrar(JPanel panel) {

        setContentPane(panel);
        pack();
        revalidate();
        repaint();
    }


}
