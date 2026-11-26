package MVC;

import MVC.controlador.ControladorJuego;
import MVC.modelo.partida.GestorPartidas;
import MVC.modelo.partida.Partida;
import MVC.vista.PanelFinPartida;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class PruebaFinPartida {
    public static void main(String[] args) throws Exception {
        Path historial = Files.createTempFile("prueba-fin-partida-", ".csv");
        try {
            SwingUtilities.invokeAndWait(() -> {
                try {
                    GestorPartidas gestor = new GestorPartidas(historial);
                    probar(gestor, true);
                    probar(gestor, false);
                    gestor.cargar();
                    if (gestor.getHistorial().size() != 2) {
                        throw new AssertionError("El historial debe contener dos partidas");
                    }
                } catch (IOException error) {
                    throw new IllegalStateException(error);
                }
            });
            System.out.println("Correcto: victoria, derrota, historial y top 3.");
        } finally {
            Files.deleteIfExists(historial);
        }
    }

    private static void probar(GestorPartidas gestor, boolean victoria) throws IOException {
        String nombre = victoria ? "Yahir" : "Luis";
        Partida partida = new Partida(nombre);
        partida.agregarPuntos("objeto", victoria ? 300 : 450);
        for (int i = 0; i < (victoria ? 65 : 27); i++) {
            partida.contarSegundo();
        }
        if (victoria) {
            partida.registrarVictoria();
        } else {
            while (!partida.estaTerminada()) {
                partida.registrarMuerte();
                partida.confirmarReaparicion();
            }
        }

        ControladorJuego controlador = new ControladorJuego(partida, gestor);
        PanelFinPartida panel = new PanelFinPartida(null);
        controlador.finalizarPartida(panel);
        controlador.finalizarPartida(panel);
        List<String> etiquetas = new ArrayList<>();
        leerEtiquetas(panel, etiquetas);
        List<String> esperadas = List.of(nombre, victoria ? "VICTORIA" : "DERROTA",
                victoria ? "00300" : "00450", victoria ? "01:05" : "00:27", "",
                "Yahir", "", "", "00300", "", "", "01:05", "", "", "");
        // El formulario también tiene separadores decorativos con texto vacío.
        if (!etiquetas.equals(esperadas)) {
            throw new AssertionError("Datos inesperados en pantalla: " + etiquetas);
        }

        if (!GraphicsEnvironment.isHeadless()) {
            // En esta prueba se avanza con Aceptar, sin regresar al menú del juego.
            desactivarBotones(panel);
            JOptionPane.showMessageDialog(null, panel, "Prueba de " + nombre, JOptionPane.PLAIN_MESSAGE);
        }
    }

    private static void leerEtiquetas(Container panel, List<String> etiquetas) {
        for (Component componente : panel.getComponents()) {
            if (componente instanceof JLabel etiqueta) {
                etiquetas.add(etiqueta.getText());
            } else if (componente instanceof Container hijo) {
                leerEtiquetas(hijo, etiquetas);
            }
        }
    }

    private static void desactivarBotones(Container panel) {
        for (Component componente : panel.getComponents()) {
            if (componente instanceof JButton boton) {
                boton.setEnabled(false);
            } else if (componente instanceof Container hijo) {
                desactivarBotones(hijo);
            }
        }
    }
}
