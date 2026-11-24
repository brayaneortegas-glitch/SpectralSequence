import MVC.modelo.entidades.*;
import java.awt.geom.Point2D;
import java.util.List;

public class VerificarProyectil {
    static void verificar(boolean condicion) {
        if (!condicion) throw new AssertionError();
    }
    public static void main(String[] args) {
        Jugador jugador = new Jugador(0, 0, 20, 40);
        Arghon enemigo = new Arghon(1, 1, 2, 100, 0, 20, 40,
                List.of(new Point2D.Double(100, 0)));
        Proyectil bala = new Proyectil(0, 0, 3, 4, 100, 5, Proyectil.Origen.JUGADOR);
        bala.actualizar(0.5);
        verificar(Math.abs(bala.getX() - 30) < 0.0001 && Math.abs(bala.getY() - 40) < 0.0001);
        verificar(!bala.impactar(jugador) && bala.estaActivo());
        verificar(bala.impactar(enemigo) && enemigo.getSalud() == 195 && !bala.estaActivo());
        verificar(!bala.impactar(enemigo) && enemigo.getSalud() == 195);
        bala.actualizar(1);
        verificar(Math.abs(bala.getX() - 30) < 0.0001);
        Proyectil hostil = new Proyectil(0, 0, 1, 0, 100, 1, Proyectil.Origen.ENEMIGO);
        verificar(!hostil.puedeDanar(enemigo) && hostil.impactar(jugador));
        Proyectil segundo = new Proyectil(0, 0, 1, 0, 100, 1, Proyectil.Origen.ENEMIGO);
        verificar(!segundo.impactar(jugador) && !segundo.estaActivo() && jugador.getSalud() == 6);
        verificar(jugador.disparar().getOrigen() == Proyectil.Origen.JUGADOR);
        verificar(jugador.getCargaPoder() == 0);
        Proyectil pared = jugador.disparar();
        pared.destruir();
        verificar(!pared.estaActivo() && !pared.impactar(enemigo));
        System.out.println("OK: movimiento, bandos, impacto unico, invulnerabilidad y destruccion");
    }
}
