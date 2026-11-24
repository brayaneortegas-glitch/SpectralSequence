package MVC.modelo.entidades;

import MVC.modelo.mundo.Nivel;

public abstract class Enemigo extends Personaje {
    private int id;
    private int danio;
    private double cooldownAtaque;
    private double tiempoAtaqueRestante;

    public Enemigo(int id, int danio, double cooldownAtaque, double x, double y, int ancho, int alto, int saludMaxima, double velocidad) {
        super(x, y, ancho, alto, saludMaxima, velocidad);
        this.id = id;
        this.danio = danio;
        this.cooldownAtaque = cooldownAtaque;
        this.tiempoAtaqueRestante = 0.0;

    }

    public int getId() {
        return id;
    }

    public int getDanio() {
        return danio;
    }

    public double getCooldownAtaque() {
        return cooldownAtaque;
    }

    public boolean puedeAtacar() {
        return estaVivo() && tiempoAtaqueRestante <= 0.0;
    }

    protected void reiniciarCooldown() {
        this.tiempoAtaqueRestante = cooldownAtaque;
    }

    public void actualizarCooldown(double dt) {
        if (tiempoAtaqueRestante > 0.0) {
            tiempoAtaqueRestante -= dt;
            if (tiempoAtaqueRestante < 0.0) {
                tiempoAtaqueRestante = 0.0;
            }
        }
    }

    public abstract void atacar(Jugador jugador, Nivel nivel);

    public abstract void actualizar(double dt, Jugador jugador, Nivel nivel);

}
