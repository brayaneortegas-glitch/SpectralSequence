package MVC.controlador;

import MVC.vista.PanelJuego;
import MVC.vista.VentanaPrincipal;
import javax.swing.JOptionPane;
import javax.swing.Timer;
import java.io.IOException;

public class CicloJuego {
    private final Timer reloj;
    private long anterior;

    public CicloJuego(ControladorJuego controlador, PanelJuego panel, VentanaPrincipal ventana) {
        reloj = new Timer(16, evento -> {
            long ahora = System.nanoTime();
            double segundos = (ahora - anterior) / 1_000_000_000.0;
            anterior = ahora;
            try {
                controlador.actualizar(Math.min(segundos, 0.1));
                panel.repaint();
                if (controlador.getPartida().estaTerminada()) {
                    detener();
                    ventana.mostrarFinPartida(controlador);
                }
            } catch (IOException error) {
                detener();
                JOptionPane.showMessageDialog(ventana, "Revisa el historial y pulsa Aceptar para reintentar.\n" + error.getMessage());
                iniciar();
            }
        });
    }

    public void iniciar() {
        anterior = System.nanoTime();
        reloj.start();
    }

    public void detener() {
        reloj.stop();
    }
}
