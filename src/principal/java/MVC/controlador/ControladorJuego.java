package MVC.controlador;

import MVC.modelo.partida.GestorPartidas;
import MVC.modelo.partida.Partida;
import MVC.modelo.partida.RegistroPartida;
import MVC.vista.PanelDerrota;
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

    public PanelDerrota crearPanelDerrota(VentanaPrincipal ventana) throws IOException {
        RegistroPartida resultado = finalizarPartida();
        if (resultado.victoria()) {
            return null;
        }
        PanelDerrota panel = new PanelDerrota(ventana);
        panel.mostrarDerrota(resultado.nombre(), resultado.puntaje(), resultado.segundos());

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
