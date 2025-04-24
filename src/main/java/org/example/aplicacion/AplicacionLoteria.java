package org.example.aplicacion;

import javax.swing.*;

import org.example.dao.ClienteDAO;
import org.example.vista.RegistroClienteUI;
import org.example.controlador.ControladorRegistroCliente;

public class AplicacionLoteria {
    public static void main(String[] args) {
        try {
            // Aplicar look and feel del sistema
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

            // Mejorar la apariencia de los componentes
            UIManager.put("Button.arc", 10);
            UIManager.put("Component.arc", 10);
            UIManager.put("ProgressBar.arc", 10);
            UIManager.put("TextComponent.arc", 10);
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                // Crear la vista
                RegistroClienteUI vista = new RegistroClienteUI();

                // Crear el DAO (acceso a datos)
                ClienteDAO dao = new ClienteDAO();

                // Crear el controlador y conectar
                ControladorRegistroCliente controlador = new ControladorRegistroCliente(vista, dao);

                // Mostrar la vista
                vista.setVisible(true);
            }
        });
    }
}