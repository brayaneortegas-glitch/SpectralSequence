package MVC.vista;

import javax.swing.*;
import java.awt.*;

public class PanelPortada extends JPanel {

    private Image[] fondos = new Image[4];
    private int indice = 0;
    private Timer timer;

    public PanelPortada(VentanaPrincipal ventana) {

        for (int i = 0; i < 4; i++) {
            fondos[i] = new ImageIcon(getClass().getResource("/imagenes/menu/PORTADA/Portada_Oscura_fondo_" + (i + 1) + ".png")).getImage();
        }

        timer = new Timer(160, e -> {

            indice = (indice + 1) % fondos.length;

            repaint();

        });


        setPreferredSize(new Dimension(800, 600));
        setLayout(null);

        FormularioPortada formulario = new FormularioPortada();
        JPanel panelFormulario = formulario.getPanelPrincipal();
        panelFormulario.setBounds(32, 364, 736, 96);
        add(panelFormulario);

        formulario.getBotonNueva().addActionListener(e -> ventana.mostrar(new PanelNombre(ventana)));
        formulario.getBotonInstrucciones().addActionListener(e -> ventana.mostrar(new PanelInstrucciones(ventana)));
        formulario.getBotonSalir().addActionListener(e -> System.exit(0));
        formulario.getBotonClasificacion().addActionListener(e -> ventana.mostrar(new PanelClasificacion(ventana)));


    }

    @Override
    public void addNotify() {
        super.addNotify();
        timer.start();
    }

    @Override
    public void removeNotify() {
        timer.stop();
        super.removeNotify();
    }

    @Override
    protected void paintComponent(Graphics g) {

        super.paintComponent(g);
        g.drawImage(fondos[indice], 0, 0, this);


    }


}
