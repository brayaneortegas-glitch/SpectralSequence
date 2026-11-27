package MVC.controlador;

import MVC.modelo.partida.GestorPartidas;
import MVC.modelo.partida.Partida;
import MVC.vista.PanelJuego;
import MVC.vista.VentanaPrincipal;
import java.io.IOException;

public class Juego {
    public static void comenzar(String nombre, VentanaPrincipal ventana) throws IOException {
        ControladorJuego controlador = new ControladorJuego(new Partida(nombre), new GestorPartidas());
        controlador.iniciar();
        PanelJuego panel = new PanelJuego(controlador, ventana);
        ventana.mostrar(panel);
        panel.requestFocusInWindow();
    }
}
