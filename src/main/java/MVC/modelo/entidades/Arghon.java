package MVC.modelo.entidades;

import MVC.modelo.mundo.Nivel;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class Arghon extends Enemigo {

    public enum Fase {NORMAL, FURIA}

    private static final double FURIA_VELOCIDAD = 400.0;
    private static final int SALUD_BASE = 200;
    private static final double VELOCIDAD_PROYECTIL = 300.0;
    private Fase fase;
    private boolean combateActivo;
    private int umbralPendiente;
    private final double xOrigen;
    private final double yOrigen;
    private final List<Point2D.Double> posicionesTeleport;

    public Arghon(int id, int danio, double cooldownAtaque,
                  double x, double y, int ancho, int alto,
                  List<Point2D.Double> posicionesTeletransporte) {
        super(id, danio, cooldownAtaque, x, y, ancho, alto,
                SALUD_BASE, 0.0);
        this.fase = Fase.NORMAL;
        this.xOrigen = x;
        this.yOrigen = y;
        this.combateActivo = false;
        this.umbralPendiente = 150;
        this.posicionesTeleport = new ArrayList<>();

        for (Point2D.Double posicion : posicionesTeletransporte) {
            this.posicionesTeleport.add(new Point2D.Double(posicion.x, posicion.y));
        }
    }

    public void iniciarCombate() {
        if (estaDerrotado() || combateActivo) {
            return;
        }
        combateActivo = true;
        reiniciarCooldown();
    }

    @Override
    public void atacar(Jugador jugador, Nivel nivel) {
        if (!combateActivo || !puedeAtacar() || jugador == null || jugador.estaMuerto()) {
            return;
        }

        double centroX = getX() + getAncho() / 2.0;
        double centroJugadorX = jugador.getX() + jugador.getAncho() / 2.0;

        if (centroJugadorX >= centroX) {
            setDireccion(1);
        } else {
            setDireccion(-1);
        }

        double origenX = getX();
        if (getDireccion() == 1) {
            origenX += getAncho();
        }

        double origenY = getY() + getAlto() / 2.0;
        double objetivoX = centroJugadorX;
        double objetivoY = jugador.getY() + jugador.getAlto() / 2.0;
        double diferenciaX = objetivoX - origenX;
        double diferenciaY = objetivoY - origenY;
        double distancia = Math.hypot(diferenciaX, diferenciaY);

        if (distancia == 0.0) {
            return;
        }
        double direccionX = diferenciaX / distancia;
        double direccionY = diferenciaY / distancia;

        double velocidadProyectil;
        if (fase == Fase.FURIA) {
            velocidadProyectil = FURIA_VELOCIDAD;
        } else {
            velocidadProyectil = VELOCIDAD_PROYECTIL;
        }
        Proyectil proyectil = new Proyectil(origenX, origenY,
                direccionX, direccionY, velocidadProyectil,
                getDanio(), Proyectil.Origen.ENEMIGO);
        nivel.agregarProyectil(proyectil);
        reiniciarCooldown();
    }

    public void teletransportarse() {
        if (estaDerrotado()) {
            return;
        }
        List<Point2D.Double> destinos = new ArrayList<>();

        for (Point2D.Double posicion : posicionesTeleport) {
            boolean esPosicionActual = posicion.x == getX() && posicion.y == getY();
            if (!esPosicionActual) {
                destinos.add(posicion);
            }
        }
        if (destinos.isEmpty()) {
            return;
        }
        int indice = ThreadLocalRandom.current().nextInt(destinos.size());
        Point2D.Double destino = destinos.get(indice);
        establecerPosicion(destino.x, destino.y);
    }

    @Override
    public void actualizar(double dt, Jugador jugador, Nivel nivel) {
        if (!combateActivo) {
            return;
        }
        double multiplicadorVelocidad;
        if (fase == Fase.FURIA) {
            multiplicadorVelocidad = 1.7;
        } else {
            multiplicadorVelocidad = 1.0;
        }
        actualizarCooldown(dt * multiplicadorVelocidad);
        atacar(jugador, nivel);
    }

    @Override
    public boolean recibirDanio(int cantidad) {
        boolean daniado = super.recibirDanio(cantidad);
        if (!daniado) {
            return false;
        }
        if (getSalud() < 100) {
            fase = Fase.FURIA;
        }
        while (umbralPendiente > 0 && getSalud() <= umbralPendiente) {
            umbralPendiente -= 50;
            if (estaVivo()) {
                teletransportarse();
            }
        }
        return true;
    }

    @Override
    public void morir() {
        if (estaVivo()) {
            return;
        }
        combateActivo = false;
    }

    public boolean estaDerrotado() {
        return getSalud() == 0;
    }

    public boolean estaEnCombate() {
        return combateActivo;
    }

    public Fase getFase() {
        return fase;
    }

    public void reiniciarCombate() {
        restaurarSalud();
        establecerPosicion(xOrigen, yOrigen);
        setDireccion(1);
        fase = Fase.NORMAL;
        combateActivo = false;
        umbralPendiente = 150;
        reiniciarCooldown();
    }
}
