package MVC.controlador;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.HashSet;
import java.util.Set;

public class ControladorEntrada extends KeyAdapter {
    private final ControladorJuego juego;
    private final Set<Integer> pulsadas = new HashSet<>();

    public ControladorEntrada(ControladorJuego juego) {
        this.juego = juego;
    }

    @Override
    public void keyPressed(KeyEvent evento) {
        int tecla = evento.getKeyCode();
        if (!pulsadas.add(tecla)) return;
        switch (tecla) {
            case KeyEvent.VK_ESCAPE -> juego.getPartida().setPausada(!juego.getPartida().estaPausada());
            case KeyEvent.VK_SPACE, KeyEvent.VK_W -> juego.saltar();
            case KeyEvent.VK_J -> juego.disparar();
            case KeyEvent.VK_K -> juego.activarPoder();
        }
        actualizarDireccion();
    }

    @Override
    public void keyReleased(KeyEvent evento) {
        pulsadas.remove(evento.getKeyCode());
        actualizarDireccion();
    }

    public void soltarTeclas() {
        pulsadas.clear();
        juego.mover(0);
    }

    private void actualizarDireccion() {
        int direccion = 0;
        if (pulsadas.contains(KeyEvent.VK_A) || pulsadas.contains(KeyEvent.VK_LEFT)) direccion--;
        if (pulsadas.contains(KeyEvent.VK_D) || pulsadas.contains(KeyEvent.VK_RIGHT)) direccion++;
        juego.mover(direccion);
    }
}
