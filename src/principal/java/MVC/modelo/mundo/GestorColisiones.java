package MVC.modelo.mundo;


import MVC.modelo.entidades.Enemigo;
import MVC.modelo.entidades.Jugador;
import MVC.modelo.entidades.Proyectil;
import MVC.modelo.objetos.ObjetoRecogible;


import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;


public class GestorColisiones {
    public void validarMundo(Nivel nivel) {
        Jugador jugador = nivel.getJugador();
        if (jugador == null || jugador.estaMuerto()) {
            return;
        }
        Rectangle2D limitesJugador = jugador.obtenerLimites();

        for (Plataforma plataforma : nivel.getPlataformas()) {
            Rectangle2D limitesPlat = plataforma.obtenerLimites();
            if (!limitesJugador.intersects(limitesPlat)) {
                continue;
            }

            double solapeIzq = limitesJugador.getMaxX() - limitesPlat.getMinX();
            double solapeDer = limitesPlat.getMaxX() - limitesJugador.getMinX();
            double solapeArriba = limitesJugador.getMaxY() - limitesPlat.getMinY();
            double solapeAbajo = limitesPlat.getMaxY() - limitesJugador.getMinY();

            if (Math.min(solapeArriba, solapeAbajo) <= Math.min(solapeIzq, solapeDer)) {
                if (solapeArriba <= solapeAbajo) {
                    jugador.aterrizarSobre(limitesPlat.getMinY());
                    limitesJugador = jugador.obtenerLimites();
                }
            } else {
                if (solapeIzq <= solapeDer) {
                    jugador.ajustarHorizontal(limitesPlat.getMinX() - jugador.getAncho());
                } else {
                    jugador.ajustarHorizontal(limitesPlat.getMaxX());
                }
                limitesJugador = jugador.obtenerLimites();
            }
        }
    }


    public boolean tocaPinchos(Nivel nivel) {
        Jugador jugador = nivel.getJugador();
        if (jugador == null || jugador.estaMuerto()) {
            return false;
        }
        for (Trampa trampa : nivel.getTrampas()) {
            if (jugador.obtenerLimites().intersects(trampa.obtenerLimites())) {
                return true;
            }
        }
        return false;
    }


    public List<Impacto> detectarImpactos(Nivel nivel) {
        List<Impacto> impactos = new ArrayList<>();
        for (Proyectil proyectil : nivel.getProyectiles()) {
            if (!proyectil.estaActivo()) {
                continue;
            }


            if (tocaPlataforma(proyectil, nivel)) {
                proyectil.destruir();
                continue;
            }


            Rectangle2D limites = proyectil.obtenerLimites();
            if (proyectil.getOrigen() == Proyectil.Origen.JUGADOR) {
                for (Enemigo enemigo : nivel.getEnemigos()) {
                    if (enemigo.estaVivo() && limites.intersects(enemigo.obtenerLimitesImpacto())) {
                        impactos.add(new Impacto(proyectil, enemigo));
                        break;
                    }
                }
            } else {
                Jugador jugador = nivel.getJugador();
                if (jugador != null && jugador.estaVivo()
                        && limites.intersects(jugador.obtenerLimitesImpacto())) {
                    impactos.add(new Impacto(proyectil, jugador));
                }
            }
        }
        return impactos;
    }


    private boolean tocaPlataforma(Proyectil proyectil, Nivel nivel) {
        for (Plataforma plataforma : nivel.getPlataformas()) {
            if (proyectil.obtenerLimites().intersects(plataforma.obtenerLimites())) {
                return true;
            }
        }
        return false;
    }


    public int detectarPuntoControl(Nivel nivel) {
        Jugador jugador = nivel.getJugador();
        if (jugador == null || jugador.estaMuerto()) {
            return -1;
        }
        int numeroDetectado = -1;
        for (PuntoControl puntoControl : nivel.getPuntosControl()) {
            if (jugador.obtenerLimites().intersects(puntoControl.obtenerLimites())
                    && puntoControl.getNumero() > numeroDetectado) {
                numeroDetectado = puntoControl.getNumero();
            }
        }
        return numeroDetectado;
    }


    public List<ObjetoRecogible> detectarObjetos(Nivel nivel) {
        List<ObjetoRecogible> encontrados = new ArrayList<>();
        Jugador jugador = nivel.getJugador();
        if (jugador == null || jugador.estaMuerto()) {
            return encontrados;
        }
        for (ObjetoRecogible objeto : nivel.getObjetoRecogibles()) {
            if (objeto.estaDisponible()
                    && jugador.obtenerLimites().intersects(objeto.obtenerLimites())) {
                encontrados.add(objeto);
            }
        }
        return encontrados;
    }
}

