package MVC.controlador;

import MVC.modelo.partida.GestorPartidas;
import MVC.modelo.partida.Partida;
import MVC.vista.PanelJuego;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.nio.file.Files;
import java.nio.file.Path;

public class PruebaPanelJuego {
    public static void main(String[] args) throws Exception {
        Path historial = Files.createTempFile("panel-juego-", ".csv");
        try {
            Partida partida = new Partida("Ian");
            ControladorJuego juego = new ControladorJuego(partida, new GestorPartidas(historial));
            juego.iniciar();
            PanelJuego panel = new PanelJuego(juego, null);
            panel.setSize(800, 600);
            KeyListener teclado = panel.getKeyListeners()[0];
            double inicio = juego.getNivel().getJugador().getX();
            pulsar(panel, teclado, KeyEvent.VK_D);
            juego.actualizar(0.1);
            double nuevaX = juego.getNivel().getJugador().getX();
            if (nuevaX <= inicio) throw new AssertionError("Ian debe avanzar");
            pulsar(panel, teclado, KeyEvent.VK_J);
            if (juego.getNivel().getProyectiles().isEmpty()) throw new AssertionError("Falta el disparo");
            pulsar(panel, teclado, KeyEvent.VK_ESCAPE);
            juego.actualizar(3);
            if (!partida.estaPausada() || juego.getNivel().getJugador().getX() != nuevaX
                    || partida.getSegundosJugados() != 0) throw new AssertionError("La pausa debe detener el juego");
            Graphics dibujo = new BufferedImage(800, 600, BufferedImage.TYPE_INT_RGB).getGraphics();
            panel.paint(dibujo);
            dibujo.dispose();
            System.out.println("OK: fondo, personajes, teclado, disparo y pausa.");
        } finally {
            Files.deleteIfExists(historial);
        }
    }

    private static void pulsar(PanelJuego panel, KeyListener teclado, int tecla) {
        teclado.keyPressed(new KeyEvent(panel, KeyEvent.KEY_PRESSED, 0, 0, tecla, KeyEvent.CHAR_UNDEFINED));
    }
}
