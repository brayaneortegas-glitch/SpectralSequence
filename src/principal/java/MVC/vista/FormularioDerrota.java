package MVC.vista;

import javax.swing.*;

public class FormularioDerrota {
    private JPanel panelPrincipal;
    private JPanel panelDatos;
    private JLabel valorJugador;
    private JLabel valorResultado;
    private JLabel valorPuntaje;
    private JLabel valorTiempo;
    private JPanel panelClasificacion;
    private JLabel nombre1;
    private JLabel nombre2;
    private JLabel nombre3;
    private JLabel puntos1;
    private JLabel puntos2;
    private JLabel puntos3;
    private JLabel tiempo1;
    private JLabel tiempo2;
    private JLabel tiempo3;
    private JPanel panelBoton;
    private JButton botonVolverInicio;


    public FormularioDerrota() {

        panelPrincipal.setOpaque(false);

        panelBoton.setOpaque(false);

        panelDatos.setOpaque(false);

        panelClasificacion.setOpaque(false);

        botonVolverInicio.setBorderPainted(false);
        botonVolverInicio.setContentAreaFilled(false);
        botonVolverInicio.setFocusPainted(false);

    }

    public JPanel getPanelPrincipal() {
        return panelPrincipal;
    }

    public JButton getBotonVolverInicio() {
        return botonVolverInicio;
    }

    public void setDatos(String nombre, String resultado, String puntaje, String tiempo){

        valorJugador.setText(nombre);
        valorResultado.setText(resultado);
        valorPuntaje.setText(puntaje);
        valorTiempo.setText(tiempo);

    }

    public void ponerFilaClasificacion(int fila, String nombre, String puntos, String tiempo){

        if (fila == 1){

            nombre1.setText(nombre);
            puntos1.setText(puntos);
            tiempo1.setText(tiempo);



        }else if (fila == 2){

            nombre2.setText(nombre);
            puntos2.setText(puntos);
            tiempo2.setText(tiempo);

        } else if (fila == 3) {

            nombre3.setText(nombre);
            puntos3.setText(puntos);
            tiempo3.setText(tiempo);

        }


    }





}
