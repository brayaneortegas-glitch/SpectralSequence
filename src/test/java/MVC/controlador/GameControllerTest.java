package MVC.controlador;

import MVC.modelo.entidades.Arghon;
import MVC.modelo.entidades.Proyectil;
import MVC.modelo.entidades.Spectrum;
import MVC.modelo.mundo.Nivel;
import MVC.modelo.objetos.ObjetoRecogible;
import MVC.modelo.partida.GestorPartidas;
import MVC.modelo.partida.Partida;
import MVC.modelo.partida.RegistroPartida;

import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;

public class GameControllerTest {
    public static void main(String[] args) throws Exception {
        comprobarTiempoYSalud();
        comprobarCheckpointsYDerrota();
        comprobarImpactos();
        comprobarVictoriaYRanking();
        System.out.println("OK: tiempo, pausa, salud, vidas, checkpoints, Spectrum, impactos, victoria y ranking.");
    }

    private static void comprobar(boolean condicion, String mensaje) {
        if (!condicion) {
            throw new AssertionError(mensaje);
        }
    }

    private static GestorPartidas crearGestor() throws Exception {
        Path carpeta = Files.createTempDirectory("spectral-controlador-");
        return new GestorPartidas(carpeta.resolve("historial.csv"));
    }

    private static void comprobarTiempoYSalud() throws Exception {
        Partida partida = new Partida();
        GameController controlador = new GameController(partida, crearGestor());
        controlador.iniciar();
        Nivel nivel = controlador.getNivel();
        nivel.getEnemigos().clear();
        controlador.actualizar(0.25);
        controlador.actualizar(0.25);
        comprobar(partida.getSegundosJugados() == 0, "No contar un segundo por frame");
        partida.setPausada(true);
        controlador.actualizar(5);
        comprobar(partida.getSegundosJugados() == 0, "La pausa no cuenta");
        partida.setPausada(false);
        controlador.actualizar(0.5);
        comprobar(partida.getSegundosJugados() == 1, "Conservar fracciones de segundo");
        comprobar(nivel.getJugador().getY() == 592, "El jugador sigue sobre el piso");
        nivel.agregarProyectil(new Proyectil(80, 600, 1, 0, 0, 1, Proyectil.Origen.ENEMIGO));
        controlador.actualizar(0.01);
        comprobar(nivel.getJugador().getSalud() == 6 && partida.getVidas() == 3,
                "Un impacto resta salud, no vidas");
        comprobar(nivel.getProyectiles().isEmpty(), "Eliminar la bala consumida");
    }

    private static void comprobarCheckpointsYDerrota() throws Exception {
        Partida partida = new Partida();
        GestorPartidas gestor = crearGestor();
        GameController controlador = new GameController(partida, gestor);
        controlador.iniciar();
        Nivel nivel = controlador.getNivel();
        Spectrum spectrum = (Spectrum) nivel.getEnemigos().get(0);
        spectrum.recibirDanio(5);
        nivel.getJugador().establecerPosicion(780, 592);
        controlador.actualizar(0.01);
        comprobar(partida.getCheckpointActual() == 1, "Activar checkpoint por contacto");
        nivel.getJugador().establecerPosicion(600, 592);
        controlador.actualizar(0.01);
        comprobar(partida.getVidas() == 2 && nivel.getJugador().getSalud() == 7,
                "Los pinchos descuentan una vida y restauran salud al reaparecer");
        comprobar(nivel.getJugador().getX() == 780 && nivel.getJugador().getY() == 592,
                "Reaparecer en el checkpoint activo");
        comprobar(spectrum.getSalud() == 7 && spectrum.getEstado() == Spectrum.Estado.NORMAL,
                "Conservar el daño del Spectrum");
        controlador.actualizar(0.01);
        comprobar(partida.getVidas() == 2, "No repetir la misma muerte");
        for (int i = 0; i < 2; i++) {
            nivel.getJugador().morirPorPinchos();
            controlador.actualizar(0.01);
        }
        comprobar(partida.estaEnGameOver() && nivel.getJugador().estaMuerto(), "No reaparecer sin vidas");
        controlador.actualizar(0.01);
        comprobar(gestor.getHistorial().size() == 1, "Guardar la derrota una sola vez");
    }

    private static void comprobarImpactos() throws Exception {
        Partida partida = new Partida();
        GameController controlador = new GameController(partida, crearGestor());
        controlador.iniciar();
        Nivel nivel = controlador.getNivel();
        Spectrum spectrum = (Spectrum) nivel.getEnemigos().get(0);
        nivel.agregarProyectil(new Proyectil(405, 600, 1, 0, 0, 5, Proyectil.Origen.JUGADOR));
        controlador.actualizar(0.01);
        comprobar(spectrum.getSalud() == 7, "Aplicar impacto al enemigo");
        comprobar(nivel.getJugador().getCargaPoder() == 1, "Cargar poder solo con impacto valido");
        nivel.agregarProyectil(new Proyectil(500, 645, 1, 0, 0, 5, Proyectil.Origen.JUGADOR));
        controlador.actualizar(0.01);
        comprobar(nivel.getProyectiles().isEmpty(), "Destruir proyectil contra el piso");
    }

    private static void comprobarVictoriaYRanking() throws Exception {
        Partida partida = new Partida("Brayan");
        GestorPartidas gestor = crearGestor();
        GameController controlador = new GameController(partida, gestor);
        controlador.iniciar();
        controlador.agregarRecompensaFinal("final-prueba", 125);
        controlador.agregarRecompensaFinal("bonus-prueba", 25);
        Nivel nivel = controlador.getNivel();
        Arghon arghon = nivel.getArghon();
        nivel.getEnemigos().clear();
        nivel.getEnemigos().add(arghon);
        arghon.recibirDanio(200);
        controlador.actualizar(0.01);
        for (ObjetoRecogible objeto : nivel.getObjetoRecogibles()) {
            if (objeto.getTipo() == ObjetoRecogible.Tipo.EXODO) {
                nivel.getJugador().establecerPosicion(objeto.getX(), objeto.getY());
            }
        }
        controlador.actualizar(0.01);
        comprobar(partida.hayVictoria() && partida.getPuntaje() == 150,
                "Sumar todas las recompensas antes de cerrar la partida");
        comprobar(gestor.getHistorial().get(0).puntaje() == 150, "Guardar el puntaje final");
        controlador.actualizar(1);
        comprobar(gestor.getHistorial().size() == 1 && partida.getPuntaje() == 150,
                "No duplicar puntos ni registros");

        Path archivo = Files.createTempDirectory("spectral-ranking-").resolve("historial.csv");
        GestorPartidas escritor = new GestorPartidas(archivo);
        escritor.guardar(new RegistroPartida("Ana", 10, 20, true, LocalDateTime.now()));
        GameController lector = new GameController(new Partida(), new GestorPartidas(archivo));
        comprobar(lector.getTop3().size() == 1, "Cargar ranking antes de consultarlo al iniciar");
    }
}
