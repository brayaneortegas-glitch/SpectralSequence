package MVC.modelo.mundo;

import MVC.modelo.entidades.Jugador;

import java.awt.geom.Rectangle2D;

public class GestorColisiones {
    public void validarMundo(Nivel nivel) {
        Jugador jugador = nivel.getJugador();
        if (jugador == null || jugador.estaMuerto()) {
            return;
        }
        for (Plataforma plataforma : nivel.getPlataformas()) {
            Rectangle2D jugadorLimites = jugador.obtenerLimites();
            Rectangle2D plataformaLimites = plataforma.obtenerLimites();
            if (!jugadorLimites.intersects(plataformaLimites)) {
                continue;
            }
            double impactoIzq = jugadorLimites.getMaxX() - plataformaLimites.getMinX();
            double impactoDer = plataformaLimites.getMaxX() - jugadorLimites.getMinX();
            double impactoArriba = jugadorLimites.getMaxY() - plataformaLimites.getMinY();
            double impactoAbajo = plataformaLimites.getMaxY() - jugadorLimites.getMinY();

            double menorHorizontal = Math.min(impactoIzq, impactoDer);
            double menorVertical = Math.min(impactoArriba, impactoAbajo);

            if (menorVertical <= menorHorizontal) {
                if (impactoArriba <= impactoAbajo) {
                    jugador.aterrizarSobre(plataformaLimites.getMinY());
                }
            } else if (impactoIzq <= impactoDer) {
                jugador.ajustarHorizontal(plataformaLimites.getMinX() - jugador.getAncho());
            } else {
                jugador.ajustarHorizontal(plataformaLimites.getMaxX());
            }

        }
    }
}
