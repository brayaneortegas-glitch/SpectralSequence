package MVC;

import MVC.view.PanelPortada;
import MVC.view.VentanaPrincipal;

public class Main {

    public static void main(String[] args) {

        VentanaPrincipal ventana = new VentanaPrincipal();
        ventana.setVisible(true);
        ventana.setLocationRelativeTo(null);
        ventana.mostrar(new PanelPortada(ventana));
        //ventana.mostrar(new PanelGameOver(ventana));
        //ventana.mostrar(new PanelPausa(ventana));

    }
}
