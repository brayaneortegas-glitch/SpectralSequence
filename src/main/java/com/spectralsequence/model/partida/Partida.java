package com.spectralsequence.model.partida;

public class Partida {
    private static final int MAX_VIDAS = 3;
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
        if (reaparicionPendiente || estaEnGameOver()) {
            return;
        }
            vidas = vidas - 1;
        reaparicionPendiente = vidas > 0;
    }

    public void confirmarReaparicion() {
        reaparicionPendiente = false;
    }

    public boolean recuperarVida() {
        if (estaEnGameOver() || reaparicionPendiente || vidas >= MAX_VIDAS) {
            return false;
        }
        vidas = vidas + 1;
        return true;
    }

    public int getCheckpointActual() {
        return checkpointActual;
    }

    public void activarCheckpoint(int numero) {
        // Solo se activa un punto válido y posterior al actual.
        if (numero > checkpointActual && numero <= ULTIMO_CHECKPOINT
                && !reaparicionPendiente && !estaEnGameOver()) {
            checkpointActual = numero;
        }
    }
}