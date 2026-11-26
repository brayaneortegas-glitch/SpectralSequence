package MVC;

import MVC.vista.PanelPortada;
import MVC.vista.VentanaPrincipal;

public class Principal {

    public static void main(String[] args) {

        VentanaPrincipal ventana = new VentanaPrincipal();
        ventana.setVisible(true);
        ventana.setLocationRelativeTo(null);
        ventana.mostrar(new PanelPortada(ventana));
        //ventana.mostrar(new PanelFinPartida(ventana));
        //ventana.mostrar(new PanelPausa(ventana));

    }
}
