package com.spectralsequence.model.partida;

public class Partida {
    private int vidas = 3;
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
}