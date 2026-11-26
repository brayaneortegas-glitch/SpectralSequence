package MVC.modelo.entidades;

public class Proyectil extends Entidad {
    public enum Origen {
        JUGADOR, ENEMIGO
    }

    private static final int TAMANIO = 8;
    private final int danio;
    private final double velocidad;
    private final Origen origen;
    private final double direccionX;
    private final double direccionY;
    private boolean activo;


    public Proyectil(double x, double y, double direccionX, double direccionY, double velocidad, int danio, Origen origen) {
        super(x, y, TAMANIO, TAMANIO);
        double longitud = Math.hypot(direccionX, direccionY);
        this.direccionX = direccionX / longitud;
        this.direccionY = direccionY / longitud;
        this.velocidad = velocidad;
        this.danio = danio;
        this.origen = origen;
        this.activo = true;
    }

    public void actualizar(double dt) {
        if (!activo) {
            return;
        }
        double nuevaX = getX() + direccionX * velocidad * dt;
        double nuevaY = getY() + direccionY * velocidad * dt;
        establecerPosicion(nuevaX, nuevaY);

    }

    public boolean puedeDaniar(Personaje objetivo) {
        if (!activo) return false;
        if (origen == Origen.JUGADOR) {
            return objetivo instanceof Enemigo;
        } else {
            return objetivo instanceof Jugador;
        }
    }

    public boolean impactar(Personaje objetivo) {
        if (!puedeDaniar(objetivo)) return false;
        destruir();
        return objetivo.recibirDanio(danio);
    }

    public Origen getOrigen() {
        return origen;
    }

    public boolean estaActivo() {
        return activo;
    }

    public void destruir() {
        activo = false;
    }
}
