package MVC.view;

import javax.swing.*;
import java.awt.*;

public class PanelRanking extends JPanel {

    private Image[] fondos = new Image[4];
    private int indice = 0;
    private Timer timer;


    public PanelRanking(VentanaPrincipal ventana) {

        for (int i = 0; i < 4; i++) {

            fondos[i] = new ImageIcon(getClass().getResource("/img/menu/RANKING/Ranking_fondo_" + (i + 1) + ".png")).getImage();

        }

        timer = new Timer(160, e -> {

            indice = (indice + 1) % fondos.length;

            repaint();

        });

        timer.start();

        setPreferredSize(new Dimension(800, 600));
        setLayout(null);

        FormRanking formulario = new FormRanking();
        JPanel panelFormulario = formulario.getPanelPrincipal();
        panelFormulario.setBounds(206, 256, 388, 265);
        add(panelFormulario);

        formulario.getBotonVolver().addActionListener(e -> ventana.mostrar(new PanelPortada(ventana)));


    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.drawImage(fondos[indice], 0, 0, this);

    }


}
