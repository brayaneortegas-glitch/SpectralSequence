package MVC.modelo.mundo;

import MVC.modelo.entidades.Personaje;
import MVC.modelo.entidades.Proyectil;

public class Impacto {
    private final Proyectil proyectil;
    private final Personaje objetivo;

    public Impacto(Proyectil proyectil, Personaje objetivo) {
        this.proyectil = proyectil;
        this.objetivo = objetivo;
    }

    public Proyectil getProyectil() {
        return proyectil;
    }

    public Personaje getObjetivo() {
        return objetivo;
    }
}
