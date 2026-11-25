package MVC.view;

import javax.swing.*;
import java.awt.*;

public class PanelNombre extends JPanel {

    private Image fondo;

    public PanelNombre(VentanaPrincipal ventana) {

        fondo = new ImageIcon(getClass().getResource("/img/menu/NOMBRE/Pantalla_Nombre_fondo_1.png")).getImage();

        setPreferredSize(new Dimension(800, 600));
        setLayout(null);

        FormNombre formulario = new FormNombre();
        JPanel panelFormulario = formulario.getPanelPrincipal();
        panelFormulario.setBounds(204, 264, 392, 204);
        add(panelFormulario);

        formulario.getBotonVolver().addActionListener(e -> ventana.mostrar(new PanelPortada(ventana)));

        formulario.getBotonComenzar().addActionListener(e -> {

            String nombre = formulario.getCampoNombre().getText().trim();

            if (nombre.isEmpty()) {

                JOptionPane.showMessageDialog(this, "Escribe tu nombre para comenzar");

            } else if (nombre.length() > 12) {

                JOptionPane.showMessageDialog(this, "El nombre puede tener maximo 12 caracteres");


            } else if (nombre.contains(";")) {

                JOptionPane.showMessageDialog(this, "El nombre no puede tener el simbolo ;");

            } else {

                JOptionPane.showMessageDialog(this, "Bienvenido " + nombre);
            }


        });

        formulario.getCampoNombre().addActionListener(e -> formulario.getBotonComenzar().doClick());


    }

    @Override
    protected void paintComponent(Graphics g) {

        super.paintComponent(g);
        g.drawImage(fondo, 0, 0, this);


    }


}
