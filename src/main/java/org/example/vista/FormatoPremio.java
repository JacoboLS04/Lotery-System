package org.example.vista;

import javax.swing.*;
import javax.swing.BorderFactory;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class FormatoPremio extends JFrame {

    private JTable boletosTable; // Tabla para mostrar los boletos
    private JButton btnVolver; // Botón para volver a la ventana anterior
    private String encargado;

    public FormatoPremio(JFrame ventanaAnterior, String encargado, DefaultTableModel modeloBoletos) {
        super("Formato Premio");
        this.encargado = encargado;
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 650);

        // Panel principal con borde azul
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createLineBorder(new Color(0, 70, 140), 2));

        // Panel de cabecera con título
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));

        JLabel titleLabel = new JLabel("FORMATO PAGO PREMIO", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 14));
        headerPanel.add(titleLabel, BorderLayout.CENTER);

        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // Panel central con la tabla de boletos
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBorder(BorderFactory.createTitledBorder("Seleccionar Boleto"));

        // Usar el modelo de la tabla de CompraBoletosUI
        boletosTable = new JTable(modeloBoletos);
        JScrollPane scrollPane = new JScrollPane(boletosTable);
        tablePanel.add(scrollPane, BorderLayout.CENTER);

        mainPanel.add(tablePanel, BorderLayout.CENTER);

        // Panel de botones finales
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        btnVolver = new JButton("Volver");
        btnVolver.setBackground(new Color(180, 180, 180));
        btnVolver.addActionListener(e -> {
            ventanaAnterior.setVisible(true); // Reabrir la ventana anterior
            this.dispose(); // Cerrar esta ventana
        });
        buttonsPanel.add(btnVolver);

        mainPanel.add(buttonsPanel, BorderLayout.SOUTH);

        // Añadir el panel principal al frame
        setContentPane(mainPanel);
        setLocationRelativeTo(null);
    }
}