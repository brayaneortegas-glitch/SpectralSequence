package MVC.modelo.partida;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

public class Partida {
    private static final int MAX_VIDAS = 3;
    private int segundosJugados = 0;
    private boolean pausada = false;
    private boolean victoria = false;
    private static final int ULTIMO_PUNTO_CONTROL = 2;
    private int puntoControlActual = 0;
    private int vidas = MAX_VIDAS;
    private boolean reaparicionPendiente = false;

    private final String nombreJugador;
    private final Set<String> eventosPuntuados = new HashSet<>();
    private int puntaje;
    private RegistroPartida resultado;

    public Partida() {
        this("Jugador");
    }

    public Partida(String nombreJugador) {
        this.nombreJugador = RegistroPartida.validarNombre(nombreJugador);
    }

    public String getNombreJugador() {
        return nombreJugador;
    }

    public int getPuntaje() {
        return puntaje;
    }

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

    public RegistroPartida crearRegistro() {
        if (!estaTerminada()) {
            throw new IllegalStateException("La partida todavía está en curso");
        }
        if (resultado == null) {
            resultado = new RegistroPartida(nombreJugador, puntaje, segundosJugados, victoria, LocalDateTime.now());
        }
        return resultado;
    }

    public int getVidas() {
        return vidas;
    }

    public boolean hayReaparicionPendiente() {
        return reaparicionPendiente;
    }

    public boolean estaEnFinPartida() {
        return vidas == 0;
    }

    public void registrarMuerte() {
        if (reaparicionPendiente || estaTerminada()) {
            return;
        }
        vidas = vidas - 1;
        reaparicionPendiente = vidas > 0;
    }

    public void confirmarReaparicion() {
        reaparicionPendiente = false;
    }

    public boolean recuperarVida() {
        if (estaTerminada() || reaparicionPendiente || vidas >= MAX_VIDAS) {
            return false;
        }
        vidas = vidas + 1;
        return true;
    }

    public int getPuntoControlActual() {
        return puntoControlActual;
    }

    public void activarPuntoControl(int numero) {
        if (numero > puntoControlActual && numero <= ULTIMO_PUNTO_CONTROL
                && !reaparicionPendiente && !estaTerminada()) {
            puntoControlActual = numero;
        }
    }

    public int getSegundosJugados() {
        return segundosJugados;
    }

    public boolean estaPausada() {
        return pausada;
    }

    public void setPausada(boolean pausada) {
        this.pausada = pausada;
    }

    public void contarSegundo() {
        if (!pausada && !estaTerminada()) {
            segundosJugados = segundosJugados + 1;
        }
    }

    public boolean hayVictoria() {
        return victoria;
    }

    public boolean estaTerminada() {
        return victoria || estaEnFinPartida();
    }

    public boolean registrarVictoria() {
        if (estaTerminada() || pausada || reaparicionPendiente) {
            return false;
        }

        victoria = true;
        return true;
    }
}
