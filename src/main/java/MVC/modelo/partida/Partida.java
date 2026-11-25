package MVC.modelo.partida;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

/** Lleva las vidas, el progreso y el puntaje de un jugador durante una partida. */
public class Partida {
    private static final int MAX_VIDAS = 3;
    private int segundosJugados = 0;
    private boolean pausada = false;
    private boolean victoria = false;
    private static final int ULTIMO_CHECKPOINT = 2;
    private int checkpointActual = 0;
    private int vidas = MAX_VIDAS;
    private boolean reaparicionPendiente = false;

    private final String nombreJugador;
    private final Set<String> eventosPuntuados = new HashSet<>();
    private int puntaje;
    private RegistroPartida resultado;

    /** Inicia una partida con el nombre Jugador. */
    public Partida() {
        this("Jugador");
    }

    /**
     * Inicia una partida con tres vidas.
     * @param nombreJugador nombre ingresado en el menú
     */
    public Partida(String nombreJugador) {
        this.nombreJugador = RegistroPartida.validarNombre(nombreJugador);
    }

    /** {@return nombre del jugador} */
    public String getNombreJugador() {
        return nombreJugador;
    }

    /** {@return puntos acumulados en esta partida} */
    public int getPuntaje() {
        return puntaje;
    }

    /**
     * Suma los puntos de una recompensa una sola vez.
     * @param evento clave que distingue esa recompensa de las demás
     * @param cantidad puntos obtenidos
     * @return verdadero si se sumaron los puntos
     */
    public boolean agregarPuntos(String evento, int cantidad) {
        if (pausada || estaTerminada() || reaparicionPendiente || evento == null
                || evento.isBlank() || cantidad <= 0 || cantidad > Integer.MAX_VALUE - puntaje) {
            return false;
        }
        if (!eventosPuntuados.add(evento)) {
            return false;
        }
        puntaje += cantidad;
        return true;
    }

    /**
     * Entrega el mismo resultado cada vez que se consulta el cierre.
     * @return resultado final para guardar en el historial
     * @throws IllegalStateException si el jugador todavía está jugando
     */
    public RegistroPartida crearRegistro() {
        if (!estaTerminada()) {
            throw new IllegalStateException("La partida todavía está en curso");
        }
        if (resultado == null) {
            resultado = new RegistroPartida(nombreJugador, puntaje, segundosJugados, victoria, LocalDateTime.now());
        }
        return resultado;
    }

    /** {@return vidas restantes} */
    public int getVidas() {
        return vidas;
    }

    /** {@return verdadero si falta colocar al jugador en el checkpoint} */
    public boolean hayReaparicionPendiente() {
        return reaparicionPendiente;
    }

    /** {@return verdadero si se agotaron las vidas} */
    public boolean estaEnGameOver() {
        return vidas == 0;
    }

    /** Descuenta una vida una sola vez por muerte. */
    public void registrarMuerte() {
        if (reaparicionPendiente || estaTerminada()) {
            return;
        }
        vidas = vidas - 1;
        reaparicionPendiente = vidas > 0;
    }

    /** Indica que el jugador ya volvió al escenario. */
    public void confirmarReaparicion() {
        reaparicionPendiente = false;
    }

    /** {@return verdadero si se recuperó una vida} */
    public boolean recuperarVida() {
        if (estaTerminada() || reaparicionPendiente || vidas >= MAX_VIDAS) {
            return false;
        }
        vidas = vidas + 1;
        return true;
    }

    /** {@return número del último checkpoint alcanzado} */
    public int getCheckpointActual() {
        return checkpointActual;
    }

    /** Avanza al checkpoint indicado.
     * @param numero número del checkpoint del nivel */
    public void activarCheckpoint(int numero) {
        if (numero > checkpointActual && numero <= ULTIMO_CHECKPOINT
                && !reaparicionPendiente && !estaTerminada()) {
            checkpointActual = numero;
        }
    }

    /** {@return tiempo de juego en segundos} */
    public int getSegundosJugados() {
        return segundosJugados;
    }

    /** {@return verdadero si el juego está en pausa} */
    public boolean estaPausada() {
        return pausada;
    }

    /** Cambia el estado de pausa.
     * @param pausada verdadero para pausar */
    public void setPausada(boolean pausada) {
        this.pausada = pausada;
    }

    /** Cuenta un segundo de juego activo. */
    public void contarSegundo() {
        if (!pausada && !estaTerminada()) {
            segundosJugados = segundosJugados + 1;
        }
    }

    /** {@return verdadero si el jugador ganó} */
    public boolean hayVictoria() {
        return victoria;
    }

    /** {@return verdadero tras una victoria o al agotar las vidas} */
    public boolean estaTerminada() {
        return victoria || estaEnGameOver();
    }

    /** {@return verdadero si se registró la victoria en este momento} */
    public boolean registrarVictoria() {
        if (estaTerminada() || pausada || reaparicionPendiente) {
            return false;
        }

        victoria = true;
        return true;
    }
}
