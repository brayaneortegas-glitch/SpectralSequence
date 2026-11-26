package MVC;

import MVC.controlador.ControladorJuego;
import MVC.modelo.partida.GestorPartidas;
import MVC.modelo.partida.Partida;
import MVC.vista.PanelDerrota;
import MVC.vista.PanelVictoria;
import MVC.vista.PanelPortada;
import MVC.vista.VentanaPrincipal;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class PruebaFinPartida {
    public static void main(String[] args) throws Exception {
        boolean automatico = args.length > 0 && args[0].equals("--automatico");
        Path historial = Files.createTempFile("prueba-fin-partida-", ".csv");
        try {
            SwingUtilities.invokeAndWait(() -> {
                try {
                    GestorPartidas gestor = new GestorPartidas(historial);
                    VentanaPrincipal ventana = GraphicsEnvironment.isHeadless() ? null : new VentanaPrincipal();
                    try {
                        probar(gestor, true, ventana, automatico);
                        probar(gestor, false, ventana, automatico);
                    } finally {
                        if (ventana != null) ventana.dispose();
                    }
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

    private static void probar(GestorPartidas gestor, boolean victoria, VentanaPrincipal ventana, boolean automatico) throws IOException {
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
        JPanel panel;
        if (ventana == null) {
            panel = controlador.crearPanelResultado(null);
        } else {
            ventana.mostrarFinPartida(controlador);
            ventana.setVisible(true);
            panel = (JPanel) ventana.getContentPane();
        }
        controlador.finalizarPartida();
        if (victoria && !(panel instanceof PanelVictoria) || !victoria && !(panel instanceof PanelDerrota)) {
            throw new AssertionError("Pantalla incorrecta para " + nombre);
        }
        List<String> etiquetas = new ArrayList<>();
        leerEtiquetas(panel, etiquetas);
        List<String> esperadas = List.of(nombre, victoria ? "VICTORIA" : "DERROTA",
                victoria ? "00300" : "00450", victoria ? "01:05" : "00:27", "",
                "Yahir", "", "", "00300", "", "", "01:05", "", "", "");
        // El formulario también tiene separadores decorativos con texto vacío.
        if (!etiquetas.equals(esperadas)) {
            throw new AssertionError("Datos inesperados en pantalla: " + etiquetas);
        }

        if (ventana != null) {
            if (!automatico) {
                JOptionPane.showMessageDialog(ventana, "Revisa el resultado de " + nombre
                    + ". Al aceptar se probará el botón de volver al inicio.");
            }
            pulsarBotonVolver(panel);
            if (!(ventana.getContentPane() instanceof PanelPortada)) {
                throw new AssertionError("El botón no volvió a la portada");
            }
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

    private static void pulsarBotonVolver(Container panel) {
        for (Component componente : panel.getComponents()) {
            if (componente instanceof JButton boton) {
                boton.doClick();
            } else if (componente instanceof Container hijo) {
                pulsarBotonVolver(hijo);
            }
        }
    }
}
