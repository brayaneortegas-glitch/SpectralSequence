package MVC.modelo.entidades;

import MVC.modelo.mundo.Nivel;

public class Spectrum extends Enemigo {

    public Spectrum(int id, int danio, double cooldownAtaque, double ataqueRestante, double x, double y, int ancho, int alto, int saludMaxima, double velocidad) {
        super(id, danio, cooldownAtaque, ataqueRestante, x, y, ancho, alto, saludMaxima, velocidad);
    }

    @Override
    public void atacar(Jugador jugador, Nivel nivel) {

    }

    @Override
    public void actualizar(double dt, Jugador jugador, Nivel nivel) {

    }

    @Override
    public void morir() {

    }
}
