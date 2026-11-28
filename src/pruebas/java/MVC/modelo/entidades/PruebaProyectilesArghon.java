package MVC.modelo.entidades;

import MVC.modelo.mundo.GestorColisiones;
import MVC.modelo.mundo.Impacto;
import MVC.modelo.mundo.Nivel;

import java.awt.geom.Rectangle2D;
import java.util.List;

public class PruebaProyectilesArghon {
    public static void main(String[] args) {
        comprobarImpactoEnCuerpo(1);
        comprobarImpactoEnCuerpo(-1);
        comprobarSalidaYDimensiones(1);
        comprobarSalidaYDimensiones(-1);
        System.out.println("OK: margen de Arghon, impacto unico en el cuerpo y balas delgadas de Ian.");
    }

    private static void comprobarImpactoEnCuerpo(int direccion) {
        Nivel nivel = new Nivel();
        nivel.cargar();
        nivel.getEnemigos().clear();
        nivel.getPlataformas().clear();
        Arghon arghon = new Arghon(20, 1, 2, 300, 100, 160, 200, List.of());
        arghon.setDireccion(direccion);
        nivel.getEnemigos().add(arghon);
        double x = direccion == 1 ? arghon.getX() : arghon.getX() + arghon.getAncho() - 8;
        Proyectil bala = new Proyectil(x, 250, direccion, 0, 500, 3, Proyectil.Origen.JUGADOR);
        nivel.agregarProyectil(bala);
        GestorColisiones gestor = new GestorColisiones();
        comprobar(bala.obtenerLimites().intersects(arghon.obtenerLimites()), "La bala entra en la celda del sprite");
        comprobar(gestor.detectarImpactos(nivel).isEmpty() && bala.estaActivo(),
                "Entrar en la celda no debe consumir la bala ni causar dano");
        int saludInicial = arghon.getSalud();
        List<Impacto> impactos = List.of();
        for (int paso = 0; paso < 60 && impactos.isEmpty(); paso++) {
            bala.actualizar(0.005);
            impactos = gestor.detectarImpactos(nivel);
        }
        comprobar(impactos.size() == 1 && impactos.get(0).getObjetivo() == arghon,
                "La bala debe alcanzar el cuerpo desde ambos lados");
        comprobar(arghon.getSalud() == saludInicial, "Detectar un impacto no duplica el dano");
        comprobar(bala.impactar(arghon) && !bala.estaActivo(), "La bala se consume al golpear el cuerpo");
        comprobar(arghon.getSalud() == saludInicial - 3, "Aplicar el dano del disparo una sola vez");
        comprobar(!bala.impactar(arghon) && arghon.getSalud() == saludInicial - 3,
                "Una bala consumida no vuelve a causar dano");
    }

    private static void comprobarSalidaYDimensiones(int direccion) {
        Jugador ian = new Jugador(100, 200, 80, 70);
        ian.setDireccion(direccion);
        Proyectil bala = ian.disparar();
        Proyectil enemiga = new Proyectil(100, 100, 0, 1, 300, 1, Proyectil.Origen.ENEMIGO);
        comprobar(bala.getAncho() == 8 && bala.getAlto() == 3, "El disparo de Ian debe medir 8 x 3");
        comprobar(enemiga.getAncho() == 8 && enemiga.getAlto() == 8, "La lluvia conserva sus dimensiones");
        Rectangle2D cuerpo = ian.obtenerLimitesImpacto();
        comprobar(direccion == 1 ? bala.getX() == cuerpo.getMaxX()
                        : bala.getX() + bala.getAncho() == cuerpo.getMinX(),
                "La bala debe salir junto al cuerpo en ambas direcciones");
        comprobar(bala.getY() + bala.getAlto() / 2.0 == ian.getY() + ian.getAlto() * 0.45,
                "Reducir el grosor conserva el centro vertical del disparo");
        double inicioX = bala.getX();
        double inicioY = bala.getY();
        bala.actualizar(0.02);
        comprobar((bala.getX() - inicioX) * direccion > 0 && bala.getY() == inicioY,
                "La bala delgada sigue viajando horizontalmente hacia donde mira Ian");
    }

    private static void comprobar(boolean condicion, String mensaje) {
        if (!condicion) throw new AssertionError(mensaje);
    }
}
