package MVC.modelo.mundo;

import MVC.modelo.entidades.Jugador;

import javax.imageio.ImageIO;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PruebaPlataformas {
    public static void main(String[] args) throws Exception {
        Nivel nivel = new Nivel();
        nivel.cargar();
        BufferedImage mapa = ImageIO.read(Objects.requireNonNull(
                PruebaPlataformas.class.getResource("/imagenes/nivel/Nivel_plataformas.png")));
        guardarGuia(nivel, mapa);
        comprobarEscala(nivel, mapa);
        comprobarApoyoSinSolape(nivel, "inicio");
        for (PuntoControl punto : nivel.getPuntosControl()) {
            nivel.reiniciarPuntoControl(punto.getNumero());
            comprobarApoyoSinSolape(nivel, "punto de control " + punto.getNumero());
        }
        comprobarAterrizajes(nivel);
        System.out.println("OK: escala del mapa, inicio, checkpoints y aterrizaje en plataformas.");
        System.out.println("Guia visual: target/codex-plataformas.png");
    }

    private static void comprobarEscala(Nivel nivel, BufferedImage mapa) {
        double escalaX = (double) nivel.getAncho() / mapa.getWidth();
        double escalaY = (double) nivel.getAlto() / mapa.getHeight();
        comprobar(escalaX == escalaY && escalaX > 0, "El mapa debe conservar sus proporciones");
        for (Plataforma plataforma : nivel.getPlataformas()) {
            Rectangle2D caja = plataforma.obtenerLimites();
            comprobar(caja.getMinX() >= 0 && caja.getMinY() >= 0
                            && caja.getMaxX() <= nivel.getAncho() && caja.getMaxY() <= nivel.getAlto(),
                    "Todas las plataformas deben quedar dentro de la imagen del nivel");
        }
    }

    private static void comprobarApoyoSinSolape(Nivel nivel, String lugar) {
        Rectangle2D ian = nivel.getJugador().obtenerLimites();
        boolean apoyado = false;
        for (Plataforma plataforma : nivel.getPlataformas()) {
            Rectangle2D suelo = plataforma.obtenerLimites();
            comprobar(!ian.intersects(suelo), "Ian no debe aparecer dentro de una plataforma: " + lugar);
            if (Math.abs(ian.getMaxY() - suelo.getMinY()) < 0.001
                    && ian.getMinX() >= suelo.getMinX() && ian.getMaxX() <= suelo.getMaxX()) {
                apoyado = true;
            }
        }
        comprobar(apoyado, "Ian debe aparecer apoyado sobre una plataforma: " + lugar);
    }

    private static void comprobarAterrizajes(Nivel nivel) {
        List<Plataforma> plataformas = new ArrayList<>(nivel.getPlataformas());
        Jugador ian = nivel.getJugador();
        GestorColisiones colisiones = new GestorColisiones();
        double alturaPiso = plataformas.get(0).getY();
        boolean hayElevada = false;
        boolean hayPeldaño = false;
        try {
            for (Plataforma plataforma : plataformas) {
                // Los muros verticales no son superficies de aterrizaje.
                if (plataforma.getAlto() > plataforma.getAncho()) continue;
                hayElevada |= plataforma.getY() < alturaPiso;
                hayPeldaño |= plataforma.getY() < alturaPiso && plataforma.getAncho() < ian.getAncho();
                nivel.getPlataformas().clear();
                nivel.getPlataformas().add(plataforma);
                double x = plataforma.getX() + (plataforma.getAncho() - ian.getAncho()) / 2.0;
                ian.establecerPosicion(x, plataforma.getY() - ian.getAlto() + 1);
                colisiones.validarMundo(nivel);
                comprobar(ian.getY() + ian.getAlto() == plataforma.getY(),
                        "Ian debe aterrizar sobre la superficie en x=" + plataforma.getX());
                comprobar(ian.getX() == x, "Aterrizar no debe desplazar a Ian hacia los lados");
            }
        } finally {
            nivel.getPlataformas().clear();
            nivel.getPlataformas().addAll(plataformas);
        }
        comprobar(hayElevada, "Deben existir plataformas elevadas ademas del piso");
        comprobar(hayPeldaño, "Deben existir los pequenos peldaños del recorrido");
    }

    private static void guardarGuia(Nivel nivel, BufferedImage mapa) throws Exception {
        BufferedImage guia = new BufferedImage(nivel.getAncho(), nivel.getAlto(), BufferedImage.TYPE_INT_RGB);
        Graphics2D g = guia.createGraphics();
        try {
            g.setColor(new Color(28, 31, 40));
            g.fillRect(0, 0, guia.getWidth(), guia.getHeight());
            g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
            g.drawImage(mapa, 0, 0, nivel.getAncho(), nivel.getAlto(), null);
            g.setFont(new Font(Font.MONOSPACED, Font.BOLD, 16));
            g.setStroke(new BasicStroke(2));
            int indice = 0;
            for (Plataforma plataforma : nivel.getPlataformas()) {
                g.setColor(Color.GREEN);
                g.draw(plataforma.obtenerLimites());
                int x = (int) plataforma.getX();
                int y = (int) plataforma.getY();
                int etiquetaY = Math.max(17, y - 5);
                g.setColor(Color.BLACK);
                g.fillRect(x + 2, etiquetaY - 14, 32, 18);
                g.setColor(Color.GREEN);
                g.drawString(Integer.toString(indice++), x + 3, etiquetaY);
            }
            g.setColor(Color.CYAN);
            g.draw(nivel.getJugador().obtenerLimites());
            g.setColor(Color.MAGENTA);
            for (PuntoControl punto : nivel.getPuntosControl()) g.draw(punto.obtenerLimites());
        } finally {
            g.dispose();
        }
        Path salida = Path.of("target", "codex-plataformas.png");
        Files.createDirectories(salida.getParent());
        ImageIO.write(guia, "png", salida.toFile());
    }

    private static void comprobar(boolean condicion, String mensaje) {
        if (!condicion) throw new AssertionError(mensaje);
    }
}
