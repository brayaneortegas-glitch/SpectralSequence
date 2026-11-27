package MVC.modelo.entidades;

import MVC.modelo.mundo.Nivel;

public class PruebaArghon {
    public static void main(String[] args) {
        comprobarOleadaYCooldown();
        comprobarBordes();
        comprobarFuriaYDerrota();
        System.out.println("OK: lluvia de Arghon, trayectoria vertical, bordes, cooldown y furia.");
    }

    private static Nivel crearNivel() {
        Nivel nivel = new Nivel();
        nivel.cargar();
        nivel.getJugador().establecerPosicion(1800, 570);
        return nivel;
    }

    private static void comprobarOleadaYCooldown() {
        Nivel nivel = crearNivel();
        Arghon arghon = nivel.getArghon();
        Jugador jugador = nivel.getJugador();
        arghon.atacar(jugador, nivel);
        comprobar(nivel.getProyectiles().isEmpty(), "No atacar antes de iniciar el combate");
        arghon.iniciarCombate();
        arghon.actualizar(arghon.getCooldownAtaque() / 2.0, jugador, nivel);
        comprobar(nivel.getProyectiles().isEmpty(), "Respetar la espera inicial");
        arghon.actualizar(arghon.getCooldownAtaque() / 2.0, jugador, nivel);
        int cantidad = nivel.getProyectiles().size();
        comprobar(cantidad >= 3, "La lluvia debe contener varios proyectiles");
        comprobar(nivel.getProyectiles().get(0).getX() < jugador.getX()
                        && nivel.getProyectiles().get(cantidad - 1).getX() > jugador.getX() + jugador.getAncho(),
                "La oleada debe cubrir ambos lados de Ian");

        double anteriorFinX = Double.NEGATIVE_INFINITY;
        for (Proyectil proyectil : nivel.getProyectiles()) {
            comprobar(proyectil.getOrigen() == Proyectil.Origen.ENEMIGO, "La lluvia pertenece al enemigo");
            comprobar(proyectil.getY() + proyectil.getAlto() < jugador.getY(), "Dar espacio para reaccionar");
            comprobar(proyectil.getX() - anteriorFinX > jugador.getAncho(), "Dejar huecos para esquivar");
            anteriorFinX = proyectil.getX() + proyectil.getAncho();
            double x = proyectil.getX();
            double y = proyectil.getY();
            proyectil.actualizar(0.1);
            comprobar(proyectil.getX() == x && proyectil.getY() > y, "Los proyectiles deben caer verticalmente");
        }
        arghon.atacar(jugador, nivel);
        comprobar(nivel.getProyectiles().size() == cantidad, "No duplicar la oleada durante el cooldown");
        arghon.actualizar(arghon.getCooldownAtaque(), jugador, nivel);
        comprobar(nivel.getProyectiles().size() == cantidad * 2, "Repetir la lluvia al terminar el cooldown");
    }

    private static void comprobarBordes() {
        Nivel nivel = crearNivel();
        Jugador jugador = nivel.getJugador();
        Arghon arghon = nivel.getArghon();
        arghon.iniciarCombate();
        double[] posiciones = {0.0, nivel.getAncho() - jugador.getAncho()};
        for (double x : posiciones) {
            nivel.getProyectiles().clear();
            jugador.establecerPosicion(x, 100);
            arghon.actualizar(arghon.getCooldownAtaque(), jugador, nivel);
            comprobar(!nivel.getProyectiles().isEmpty(), "Disparar tambien cerca de los bordes");
            for (Proyectil proyectil : nivel.getProyectiles()) {
                comprobar(proyectil.getX() >= 0 && proyectil.getX() + proyectil.getAncho() <= nivel.getAncho(),
                        "Mantener la oleada dentro de los limites horizontales");
                comprobar(proyectil.getY() >= 0 && proyectil.getY() < jugador.getY(),
                        "Mantener el origen en el mundo y sobre Ian");
            }
        }
    }

    private static void comprobarFuriaYDerrota() {
        Nivel nivel = crearNivel();
        Arghon arghon = nivel.getArghon();
        Jugador jugador = nivel.getJugador();
        arghon.iniciarCombate();
        arghon.actualizar(arghon.getCooldownAtaque(), jugador, nivel);
        Proyectil normal = nivel.getProyectiles().get(0);
        double yNormal = normal.getY();
        normal.actualizar(0.1);
        double avanceNormal = normal.getY() - yNormal;
        nivel.getProyectiles().clear();

        arghon.recibirDanio(101);
        comprobar(arghon.getFase() == Arghon.Fase.FURIA, "Entrar en furia al bajar de 100 de salud");
        arghon.actualizar(arghon.getCooldownAtaque() * 0.75, jugador, nivel);
        comprobar(!nivel.getProyectiles().isEmpty(), "En furia el intervalo es menor al normal");
        Proyectil furia = nivel.getProyectiles().get(0);
        double yFuria = furia.getY();
        furia.actualizar(0.1);
        comprobar(furia.getY() - yFuria > avanceNormal, "La lluvia cae mas rapido en furia");

        arghon.recibirDanio(arghon.getSalud());
        nivel.getProyectiles().clear();
        arghon.actualizar(10, jugador, nivel);
        comprobar(nivel.getProyectiles().isEmpty(), "Arghon derrotado no crea mas oleadas");
    }

    private static void comprobar(boolean condicion, String mensaje) {
        if (!condicion) {
            throw new AssertionError(mensaje);
        }
    }
}
