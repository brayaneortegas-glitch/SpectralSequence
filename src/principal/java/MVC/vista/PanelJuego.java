package MVC.vista;

import MVC.controlador.CicloJuego;
import MVC.controlador.ControladorEntrada;
import MVC.controlador.ControladorJuego;
import MVC.modelo.entidades.*;
import MVC.modelo.mundo.*;
import MVC.modelo.objetos.ObjetoRecogible;
import MVC.modelo.partida.Partida;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

public class PanelJuego extends JPanel {
    private final ControladorJuego controlador;
    private final CicloJuego ciclo;
    private final Image cielo = imagen("nivel/fondo/Fondo_capa_cielo.png");
    private final Image catedral = imagen("nivel/fondo/Fondo_capa_catedral.png");
    private final Image ruinas = imagen("nivel/fondo/Fondo_capa_ruinas.png");
    private final Image plataformas = imagen("nivel/Nivel_plataformas.png");
    private final Image ian = imagen("animaciones/ian-juego/Ian_caminar.png");
    private final Image poder = imagen("animaciones/ian-juego/Ian_caminar_poder.png");
    private final Image espectro = imagen("animaciones/espectro/Espectro_reposo.png");
    private final Image jefe = imagen("animaciones/arghon-juego/Arghon_reposo.png");
    private final Image vida = imagen("objetos/Vida_extra.png");
    private final Image exodo = imagen("objetos/Nexo.png");
    private final Indicadores indicadores = new Indicadores();
    private int camaraX;
    private int cuadro;


    public PanelJuego(ControladorJuego controlador, VentanaPrincipal ventana) {
        this.controlador = controlador;
        setPreferredSize(new Dimension(800, 600));
        setFocusable(true);
        ControladorEntrada entrada = new ControladorEntrada(controlador);
        addKeyListener(entrada);
        addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent evento) {
                entrada.soltarTeclas();
            }
        });
        ciclo = new CicloJuego(controlador, this, ventana);
    }

    private Image imagen(String ruta) {
        return new ImageIcon(getClass().getResource("/imagenes/" + ruta)).getImage();
    }

    @Override
    public void addNotify() {
        super.addNotify();
        ciclo.iniciar();
    }

    @Override
    public void removeNotify() {
        ciclo.detener();
        super.removeNotify();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Nivel nivel = controlador.getNivel();
        Jugador jugador = nivel.getJugador();
        Partida partida = controlador.getPartida();
        camaraX = Math.max(0, Math.min((int) jugador.getX() - 400, nivel.getAncho() - 800));
        dibujarCapa(g, cielo, 0.1);
        dibujarCapa(g, catedral, 0.3);
        dibujarCapa(g, ruinas, 0.6);
        Graphics mundo = g.create();
        mundo.translate(-camaraX, 600 - nivel.getAlto());
        mundo.drawImage(plataformas, 0, 0, nivel.getAncho(), nivel.getAlto(), this);

        for (Trampa trampa : nivel.getTrampas()) rectangulo(mundo, trampa, Color.RED);
        for (PuntoControl punto : nivel.getPuntosControl()) {
            mundo.setColor(punto.getNumero() <= partida.getPuntoControlActual() ? Color.GREEN : Color.GRAY);
            mundo.drawRect((int) punto.getX(), (int) punto.getY(), punto.getAncho(), punto.getAlto());
        }
        if (!partida.estaPausada()) cuadro = (int) ((System.nanoTime() / 120_000_000L) % 120);
        for (ObjetoRecogible objeto : nivel.getObjetoRecogibles()) {
            if (objeto.estaDisponible()) {
                boolean esVida = objeto.getTipo() == ObjetoRecogible.Tipo.VIDA;
                dibujarFicha(mundo, objeto, esVida ? vida : exodo, esVida ? 22 : 24, cuadro, false);
            }
        }
        for (Enemigo enemigo : nivel.getEnemigos()) {
            if (enemigo.estaVivo()) {
                boolean esJefe = enemigo instanceof Arghon;
                dibujarFicha(mundo, enemigo, esJefe ? jefe : espectro, esJefe ? 128 : 48, cuadro, false);
            }
        }
        for (Proyectil bala : nivel.getProyectiles()) rectangulo(mundo, bala,
                bala.getOrigen() == Proyectil.Origen.JUGADOR ? Color.YELLOW : Color.ORANGE);
        //dibujarFicha(mundo, jugador, jugador.isPoderActivo() ? poder : ian, 80, cuadro, jugador.getDireccion() < 0);

        Image hojaIan;
        if (jugador.isPoderActivo()){
            hojaIan = poder;
        }else {

            hojaIan = ian;

        }

        dibujarAnclado(mundo, jugador, hojaIan, 80, 28, 62,1,  cuadro, jugador.getDireccion() < 0);

        mundo.dispose();

        g.setColor(Color.BLACK);
        //g.fillRect(0, 0, getWidth(), 32);
        g.setColor(Color.WHITE);
        //g.drawString(partida.getNombreJugador() + "     Vidas: " + partida.getVidas() + "     Salud: " + jugador.getSalud()
       //         + "     Puntos: " + partida.getPuntaje() + "     Tiempo: " + partida.getSegundosJugados()
         //       + " s     Poder: " + jugador.getCargaPoder() + "/7", 14, 21);

        indicadores.dibujar(g,jugador.getSalud(), jugador.getCargaPoder(), partida.getVidas());
        indicadores.dibujarTexto(g, partida.getPuntaje(), partida.getSegundosJugados());

        if (partida.estaPausada()) {
            g.setColor(Color.BLACK);
            g.fillRect(275, 260, 250, 45);
            g.setColor(Color.WHITE);
            g.drawString("PAUSA    ·    Esc para continuar", 306, 287);
        }
    }

    private void dibujarCapa(Graphics g, Image imagen, double velocidad) {
        int x = -(int) (camaraX * velocidad) % 1600;
        g.drawImage(imagen, x, 0, 1600, 600, this);
        g.drawImage(imagen, x + 1600, 0, 1600, 600, this);
    }

    private void rectangulo(Graphics g, Entidad entidad, Color color) {
        g.setColor(color);
        g.fillRect((int) entidad.getX(), (int) entidad.getY(), entidad.getAncho(), entidad.getAlto());
    }

    private void dibujarFicha(Graphics g, Entidad entidad, Image hoja, int anchoCelda, int cuadro, boolean voltear) {
        int origen = cuadro % (hoja.getWidth(this) / anchoCelda) * anchoCelda;
        int x = (int) entidad.getX();
        int y = (int) entidad.getY();
        int fin = x + entidad.getAncho();
        g.drawImage(hoja, voltear ? fin : x, y, voltear ? x : fin, y + entidad.getAlto(),
                origen, 0, origen + anchoCelda, hoja.getHeight(this), this);
    }

    private  void dibujarAnclado(Graphics g, Entidad entidad, Image hoja,int anchoCelda, int anclaX, int anclaY, double escala, int cuadro, boolean voltear){

        int pieX = (int) (entidad.getX() +  entidad.getAncho() / 2.0);
        int pieY = (int) (entidad.getY() +  entidad.getAlto());
        int origen = cuadro % (hoja.getWidth(this) / anchoCelda) * anchoCelda;
        int altoCelda = hoja.getHeight(this);
        int ancla = anclaX;
        if (voltear){

            ancla = anchoCelda - anclaX;

        }
        int esquinaX = pieX - (int) (ancla * escala);
        int esquinaY = pieY - (int) (anclaY * escala);
        int anchoDestino = (int) (anchoCelda * escala);
        int altoDestino = (int) (altoCelda * escala);

        if (voltear){

            g.drawImage(hoja, esquinaX + anchoDestino, esquinaY, esquinaX, esquinaY + altoDestino, origen, 0, origen + anchoCelda, altoCelda, this);

        }else {

            g.drawImage(hoja,esquinaX, esquinaY, esquinaX + anchoDestino, esquinaY + altoDestino, origen, 0, origen + anchoCelda, altoCelda, this);

        }








    }





}
