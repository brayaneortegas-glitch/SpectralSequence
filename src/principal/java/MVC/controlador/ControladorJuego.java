package MVC.controlador;

import MVC.modelo.partida.GestorPartidas;
import MVC.modelo.partida.Partida;
import MVC.modelo.partida.RegistroPartida;
import MVC.vista.PanelDerrota;
import MVC.vista.PanelResultado;
import MVC.vista.PanelVictoria;
import MVC.vista.VentanaPrincipal;

import java.io.IOException;
import java.util.List;

public class ControladorJuego {
    private final Partida partida;
    private final GestorPartidas gestor;
    private RegistroPartida resultadoGuardado;

    public ControladorJuego(Partida partida, GestorPartidas gestor) {
        this.partida = partida;
        this.gestor = gestor;
    }

    public RegistroPartida finalizarPartida() throws IOException {
        if (resultadoGuardado == null) {
            RegistroPartida resultado = partida.crearRegistro();
            gestor.guardar(resultado);
            resultadoGuardado = resultado;
        }
        return resultadoGuardado;
    }

    public PanelResultado crearPanelResultado(VentanaPrincipal ventana) throws IOException {
        RegistroPartida resultado = finalizarPartida();
        PanelResultado panel;
        if (resultado.victoria()) {
            panel = new PanelVictoria(ventana);
        } else {
            panel = new PanelDerrota(ventana);
        }
        panel.mostrarResultado(resultado.nombre(), resultado.puntaje(), resultado.segundos());

        List<RegistroPartida> mejores = obtenerTresMejores();
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

    public List<RegistroPartida> obtenerTresMejores() {
        return gestor.obtenerTresMejores();
    }
}
