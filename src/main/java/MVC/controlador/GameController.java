package MVC.controlador;

import MVC.modelo.partida.GestorPartidas;
import MVC.modelo.partida.Partida;
import MVC.modelo.partida.RegistroPartida;
import MVC.view.PanelGameOver;

import java.io.IOException;
import java.util.List;

public class GameController {
    private final Partida partida;
    private final GestorPartidas gestor;
    private RegistroPartida resultadoGuardado;

    public GameController(Partida partida, GestorPartidas gestor) {
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

    public void finalizarPartida(PanelGameOver panel) throws IOException {
        RegistroPartida resultado = finalizarPartida();
        panel.mostrarResultado(resultado.nombre(), resultado.victoria(), resultado.puntaje(), resultado.segundos());

        List<RegistroPartida> mejores = getTop3();
        for (int fila = 1; fila <= 3; fila++) {
            if (fila <= mejores.size()) {
                RegistroPartida registro = mejores.get(fila - 1);
                panel.mostrarFilaTop(fila, registro.nombre(), registro.puntaje(), registro.segundos());
            } else {
                panel.limpiarFilaTop(fila);
            }
        }
    }

    public List<RegistroPartida> getTop3() {
        return gestor.getTop3();
    }
}
