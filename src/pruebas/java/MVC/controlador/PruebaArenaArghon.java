package MVC.controlador;

import MVC.modelo.entidades.Arghon;
import MVC.modelo.entidades.Jugador;
import MVC.modelo.mundo.Nivel;
import MVC.modelo.partida.GestorPartidas;
import MVC.modelo.partida.Partida;

import java.nio.file.Files;
import java.nio.file.Path;

public class PruebaArenaArghon {
    public static void main(String[] args) throws Exception {
        Path historial = Files.createTempFile("arena-arghon-", ".csv");
        try {
            ControladorJuego juego = new ControladorJuego(new Partida(), new GestorPartidas(historial));
            juego.iniciar();
            Nivel nivel = juego.getNivel();
            nivel.getEnemigos().removeIf(enemigo -> !(enemigo instanceof Arghon));
            comprobarPosiciones(nivel);
            comprobarEntrada(juego);
            comprobarReinicio(juego);
            comprobarLimites(nivel);
            System.out.println("OK: Arghon en la arena final, activacion al entrar, cooldown y reinicio.");
        } finally {
            Files.deleteIfExists(historial);
        }
    }

    private static void comprobarPosiciones(Nivel nivel) {
        Arghon jefe = nivel.getArghon();
        double inicioX = jefe.getX();
        double inicioY = jefe.getY();
        for (int i = 0; i < 30; i++) {
            comprobar(jefe.getX() >= 3232 && jefe.getX() + jefe.getAncho() <= 3968,
                    "Arghon y todos sus destinos deben quedar dentro de la arena final");
            comprobar(jefe.getY() + jefe.getAlto() == 536,
                    "Arghon debe quedar sobre el piso de la arena");
            jefe.teletransportarse();
        }
        jefe.reiniciarCombate();
        comprobar(jefe.getX() == inicioX && jefe.getY() == inicioY,
                "Reiniciar el combate devuelve al jefe a su posicion final inicial");
    }

    private static void comprobarEntrada(ControladorJuego juego) throws Exception {
        Nivel nivel = juego.getNivel();
        Jugador ian = nivel.getJugador();
        Arghon jefe = nivel.getArghon();
        double[] antesDeArena = {2100, 3000, 3232 - ian.getAncho(), 3231};
        for (double x : antesDeArena) {
            ian.establecerPosicion(x, 536 - ian.getAlto());
            comprobar(!nivel.jugadorEstaEnArenaArghon(), "No activar hasta cruzar completamente la entrada");
            juego.actualizar(jefe.getCooldownAtaque() + 0.1);
            comprobar(!jefe.estaEnCombate() && nivel.getProyectiles().isEmpty(),
                    "Arghon no debe detectar ni lanzar lluvia antes de entrar a la arena");
        }

        ian.establecerPosicion(3232, 536 - ian.getAlto());
        comprobar(nivel.jugadorEstaEnArenaArghon(), "Detectar a Ian al cruzar la entrada de la arena");
        juego.actualizar(0.01);
        comprobar(jefe.estaEnCombate(), "Entrar a la arena debe iniciar el combate");
        comprobar(nivel.getProyectiles().isEmpty(), "La primera lluvia debe respetar el cooldown inicial");
        juego.actualizar(jefe.getCooldownAtaque() + 0.01);
        comprobar(!nivel.getProyectiles().isEmpty(), "Arghon debe atacar despues de la espera inicial");
    }

    private static void comprobarReinicio(ControladorJuego juego) throws Exception {
        Nivel nivel = juego.getNivel();
        nivel.reiniciarPuntoControl(0);
        comprobar(!nivel.getArghon().estaEnCombate() && nivel.getProyectiles().isEmpty(),
                "Reaparecer debe detener el combate y limpiar la lluvia");
        juego.actualizar(nivel.getArghon().getCooldownAtaque() + 0.1);
        comprobar(!nivel.getArghon().estaEnCombate() && nivel.getProyectiles().isEmpty(),
                "El jefe no debe volver a activarse desde el checkpoint anterior");

        nivel.cargar();
        comprobar(!nivel.jugadorEstaEnArenaArghon() && !nivel.getArghon().estaEnCombate(),
                "Una nueva carga del nivel deja al jefe esperando en la arena final");
    }

    private static void comprobarLimites(Nivel nivel) {
        Jugador ian = nivel.getJugador();
        ian.establecerPosicion(3968 - ian.getAncho(), 536 - ian.getAlto());
        comprobar(nivel.jugadorEstaEnArenaArghon(), "El extremo interior derecho pertenece a la arena");
        ian.establecerPosicion(3968, 536 - ian.getAlto());
        comprobar(!nivel.jugadorEstaEnArenaArghon(), "No detectar a Ian fuera del muro derecho");
        ian.establecerPosicion(3500, 536);
        comprobar(!nivel.jugadorEstaEnArenaArghon(), "No detectar a Ian debajo del suelo");
        ian.establecerPosicion(3500, -ian.getAlto());
        comprobar(!nivel.jugadorEstaEnArenaArghon(), "No detectar a Ian fuera del limite superior");
        ian.establecerPosicion(3500, 536 - ian.getAlto());
        ian.morirPorPinchos();
        comprobar(!nivel.jugadorEstaEnArenaArghon(), "Un jugador muerto no inicia el combate");
    }

    private static void comprobar(boolean condicion, String mensaje) {
        if (!condicion) throw new AssertionError(mensaje);
    }
}
