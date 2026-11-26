package MVC.vista;

import javax.swing.*;
import java.awt.*;

public class FormularioNombre {
    private JPanel panelPrincipal;
    private JTextField campoNombre;
    private JButton botonComenzar;
    private JButton botonVolver;


    public FormularioNombre() {
        panelPrincipal.setOpaque(false); //HACE TRANSPARENTE EL PANEL
        campoNombre.setBorder(null);
        campoNombre.setCaretColor(new Color(230, 200, 120));
        botonComenzar.setBorderPainted(false);
        botonComenzar.setContentAreaFilled(false);
        botonComenzar.setFocusPainted(false);

        botonVolver.setBorderPainted(false);
        botonVolver.setContentAreaFilled(false);
        botonVolver.setFocusPainted(false);

    }

    public JPanel getPanelPrincipal() {

        return panelPrincipal;

    }

    public JTextField getCampoNombre() {

        return campoNombre;


    }

    public JButton getBotonComenzar() {

        return botonComenzar;


    }

    public JButton getBotonVolver() {

        return botonVolver;

    }


}
