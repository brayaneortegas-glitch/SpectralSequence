package MVC.controlador;

import MVC.modelo.entidades.Jugador;
import MVC.modelo.mundo.GestorColisiones;
import MVC.modelo.mundo.Nivel;
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
    private final Nivel nivel = new Nivel();
    private final GestorColisiones colisiones = new GestorColisiones();
    private double fraccionSegundo;
    private int direccion;

    public ControladorJuego(Partida partida, GestorPartidas gestor) {
        this.partida = partida;
        this.gestor = gestor;
    }

    public void iniciar() throws IOException {
        gestor.cargar();
        nivel.cargar();
    }

    public Nivel getNivel() {
        return nivel;
    }

    public Partida getPartida() {
        return partida;
    }

    public void mover(int direccion) {
        this.direccion = direccion;
    }

    public void saltar() {
        if (!partida.estaPausada() && !partida.estaTerminada()) nivel.getJugador().saltar();
    }

    public void disparar() {
        if (!partida.estaPausada() && !partida.estaTerminada()) nivel.agregarProyectil(nivel.getJugador().disparar());
    }

    public void activarPoder() {
        if (!partida.estaPausada() && !partida.estaTerminada()) nivel.getJugador().activarPoder();
    }

    public void actualizar(double dt) throws IOException {
        if (dt <= 0 || !Double.isFinite(dt) || partida.estaPausada() || partida.estaTerminada()) return;
        fraccionSegundo += dt;
        while (fraccionSegundo >= 1) {
            partida.contarSegundo();
            fraccionSegundo--;
        }
        while (dt > 0 && !partida.estaTerminada()) {
            double paso = Math.min(dt, 0.02);
            actualizarNivel(paso);
            dt -= paso;
        }
    }

    private void actualizarNivel(double paso) throws IOException {
        Jugador jugador = nivel.getJugador();
        jugador.mover(direccion, paso);
        jugador.ajustarHorizontal(Math.max(0, Math.min(jugador.getX(), nivel.getAncho() - jugador.getAncho())));
        nivel.actualizar(paso);
        colisiones.validarMundo(nivel);
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

    public List<RegistroPartida> obtenerTresMejores() throws IOException {
        gestor.cargar();
        return gestor.obtenerTresMejores();
    }
}
