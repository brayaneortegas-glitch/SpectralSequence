package MVC.modelo.mundo;

import MVC.modelo.entidades.Jugador;
import MVC.modelo.entidades.Spectrum;

public class PruebaMovimientoSpectrum {
    public static void main(String[] args) {
        comprobarParedes();
        comprobarSeparacion();
        comprobarBordes();
        comprobarEsperaRenacimiento();
        System.out.println("OK: Spectrums, paredes, separacion, limites y espera para renacer.");
    }

    private static Nivel crearNivel() {
        Nivel nivel = new Nivel();
        nivel.cargar();
        nivel.getEnemigos().clear();
        nivel.getPlataformas().clear();
        nivel.getTrampas().clear();
        nivel.getJugador().establecerPosicion(800, 330);
        return nivel;
    }

    private static Spectrum crearSpectrum(Nivel nivel, int id, double x) {
        Spectrum spectrum = new Spectrum(id, 1, 1, x, 300, 70, 100, 200, 10000, 15);
        nivel.getEnemigos().add(spectrum);
        return spectrum;
    }

    private static void comprobarParedes() {
        Nivel nivel = crearNivel();
        Spectrum spectrum = crearSpectrum(nivel, 1, 200);
        Plataforma pared = new Plataforma(400, 250, 10, 150);
        nivel.getPlataformas().add(pared);
        spectrum.perseguir(nivel.getJugador(), nivel, 10);
        comprobar(spectrum.getX() + spectrum.getAncho() == pared.getX(),
                "Frenar antes de la pared incluso con un paso muy grande");
        comprobar(!spectrum.obtenerLimites().intersects(pared.obtenerLimites()),
                "El Spectrum no termina dentro de la estructura");
        spectrum.establecerPosicion(600, 300);
        nivel.getJugador().establecerPosicion(100, 330);
        spectrum.perseguir(nivel.getJugador(), nivel, 10);
        comprobar(spectrum.getX() == pared.getX() + pared.getAncho(),
                "La pared tambien bloquea al caminar hacia la izquierda");

        nivel.getPlataformas().clear();
        nivel.getPlataformas().add(new Plataforma(0, 400, 1000, 30));
        nivel.getPlataformas().add(new Plataforma(300, 250, 200, 30));
        spectrum.establecerPosicion(200, 300);
        nivel.getJugador().establecerPosicion(800, 330);
        spectrum.perseguir(nivel.getJugador(), nivel, 1);
        comprobar(spectrum.getX() == 400,
                "Pisar el suelo o pasar bajo una plataforma libre no bloquea el movimiento");
    }

    private static void comprobarSeparacion() {
        Nivel nivel = crearNivel();
        Spectrum seguidor = crearSpectrum(nivel, 1, 200);
        Spectrum delantero = crearSpectrum(nivel, 2, 400);
        seguidor.perseguir(nivel.getJugador(), nivel, 10);
        comprobar(delantero.getX() - seguidor.getX() - seguidor.getAncho() >= 4,
                "No atravesar otro Spectrum y conservar una separacion visible");
        for (int paso = 0; paso < 100; paso++) {
            seguidor.actualizar(0.02, nivel.getJugador(), nivel);
            delantero.actualizar(0.02, nivel.getJugador(), nivel);
            comprobar(!seguidor.obtenerLimites().intersects(delantero.obtenerLimites()),
                    "Los Spectrums que persiguen al mismo objetivo no se superponen");
        }
        delantero.recibirDanio(delantero.getSalud());
        double xAnterior = seguidor.getX();
        seguidor.perseguir(nivel.getJugador(), nivel, 10);
        comprobar(seguidor.getX() > xAnterior, "Un Spectrum pendiente de renacer no es un obstaculo invisible");

        Spectrum obstaculo = crearSpectrum(nivel, 3, 500);
        seguidor.establecerPosicion(700, 300);
        nivel.getJugador().establecerPosicion(100, 330);
        seguidor.perseguir(nivel.getJugador(), nivel, 10);
        comprobar(seguidor.getX() - obstaculo.getX() - obstaculo.getAncho() >= 4,
                "Conservar separacion tambien al perseguir hacia la izquierda");
    }

    private static void comprobarBordes() {
        Nivel nivel = crearNivel();
        Spectrum spectrum = crearSpectrum(nivel, 1, 200);
        Jugador ian = nivel.getJugador();
        ian.establecerPosicion(-1000, 330);
        spectrum.perseguir(ian, nivel, 100);
        comprobar(spectrum.getX() == 0, "No salir por el borde izquierdo");
        ian.establecerPosicion(nivel.getAncho() + 1000, 330);
        spectrum.perseguir(ian, nivel, 100);
        comprobar(spectrum.getX() + spectrum.getAncho() == nivel.getAncho(),
                "No salir por el borde derecho");
    }

    private static void comprobarEsperaRenacimiento() {
        Nivel nivel = crearNivel();
        nivel.getPlataformas().add(new Plataforma(0, 400, nivel.getAncho(), 200));
        Spectrum spectrum = crearSpectrum(nivel, 1, 200);
        spectrum.recibirDanio(spectrum.getSalud());
        spectrum.actualizar(4.99, nivel.getJugador(), nivel);
        comprobar(spectrum.getEstado() == Spectrum.Estado.PENDIENTE && !spectrum.estaVivo(),
                "Esperar cinco segundos completos antes de renacer");
        spectrum.actualizar(0.02, nivel.getJugador(), nivel);
        comprobar(spectrum.getEstado() == Spectrum.Estado.RENACIDO && spectrum.estaVivo(),
                "Renacer al terminar la espera si hay un lugar seguro");
        spectrum.recibirDanio(spectrum.getSalud());
        spectrum.actualizar(10, nivel.getJugador(), nivel);
        comprobar(spectrum.getEstado() == Spectrum.Estado.ELIMINADO,
                "Conservar la eliminacion definitiva despues de la segunda derrota");
    }

    private static void comprobar(boolean condicion, String mensaje) {
        if (!condicion) throw new AssertionError(mensaje);
    }
}
