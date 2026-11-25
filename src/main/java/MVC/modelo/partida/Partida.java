package MVC.modelo.partida;

public class Partida {
    private static final int MAX_VIDAS = 3;
    private int segundosJugados = 0;
    private boolean pausada = false;
    private boolean victoria = false;
    private static final int ULTIMO_CHECKPOINT = 2;
    private int checkpointActual = 0;
    private int vidas = MAX_VIDAS;
    private boolean reaparicionPendiente = false;

    public int getVidas() {
        return vidas;
    }

    public boolean hayReaparicionPendiente() {
        return reaparicionPendiente;
    }

    public boolean estaEnGameOver() {
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

    public int getCheckpointActual() {
        return checkpointActual;
    }

    public void activarCheckpoint(int numero) {
        if (numero > checkpointActual && numero <= ULTIMO_CHECKPOINT
                && !reaparicionPendiente && !estaTerminada()) {
            checkpointActual = numero;
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
        return victoria || estaEnGameOver();
    }

    public boolean registrarVictoria() {
        if (estaTerminada() || pausada || reaparicionPendiente) {
            return false;
        }

        victoria = true;
        return true;
    }
}