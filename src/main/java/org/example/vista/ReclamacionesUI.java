package org.example.vista;

import org.example.dao.ReclamacionDAO;
import org.example.dao.ClienteDAO;
import org.example.modelo.Reclamacion;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.util.List;
import java.util.UUID;

public class ReclamacionesUI extends JFrame {
    private DefaultTableModel tableModel;
    private ReclamacionDAO reclamacionDAO;
    private ClienteDAO clienteDAO;

    // Colores para el estilo
    private static final Color HEADER_COLOR = new Color(39, 89, 149);
    private static final Color BUTTON_COLOR = new Color(87, 134, 194);
    private static final Font TITLE_FONT = new Font("Arial", Font.BOLD, 18);
    private static final Font LABEL_FONT = new Font("Arial", Font.BOLD, 12);

    public ReclamacionesUI() {
        reclamacionDAO = new ReclamacionDAO();
        clienteDAO = new ClienteDAO();
        setTitle("Reclamaciones");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Main panel con color de fondo
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        mainPanel.setBackground(Color.WHITE);

        // Panel superior con título y detalles
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Color.WHITE);

        // Título
        JLabel titleLabel = new JLabel("Gestión de Reclamaciones", SwingConstants.CENTER);
        titleLabel.setFont(TITLE_FONT);
        titleLabel.setForeground(HEADER_COLOR);

        // Información de ubicación
        JLabel locationLabel = new JLabel("Cra. 16 #19-21, Armenia, Quindío", SwingConstants.CENTER);
        locationLabel.setForeground(Color.GRAY);

        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setBackground(Color.WHITE);
        titlePanel.add(titleLabel, BorderLayout.CENTER);
        titlePanel.add(locationLabel, BorderLayout.SOUTH);
        headerPanel.add(titlePanel, BorderLayout.CENTER);

        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // Panel para la tabla con borde titulado
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBackground(Color.WHITE);
        TitledBorder tableBorder = BorderFactory.createTitledBorder("LISTA DE RECLAMACIONES");
        tableBorder.setTitleFont(LABEL_FONT);
        tableBorder.setTitleColor(HEADER_COLOR);
        tablePanel.setBorder(tableBorder);

        // Table
        String[] columnNames = {"ID", "Cédula Cliente", "Fecha", "Estado", "Mensaje"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Prevent editing
            }
        };
        JTable table = new JTable(tableModel);

        // Apply consistent styling
        table.setFont(new Font("Arial", Font.PLAIN, 12)); // Set table font
        table.setRowHeight(25); // Set row height
        table.setGridColor(Color.LIGHT_GRAY); // Set grid color
        table.setSelectionBackground(new Color(184, 207, 229)); // Set selection background color

        // Header styling
        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Arial", Font.BOLD, 12)); // Set header font
        header.setBackground(new Color(39, 89, 149)); // Set header background color
        header.setForeground(Color.WHITE); // Set header text color

        // Add table to scroll pane
        JScrollPane tableScrollPane = new JScrollPane(table);
        tablePanel.add(tableScrollPane, BorderLayout.CENTER);
        mainPanel.add(tablePanel, BorderLayout.CENTER);

        // Form panel
        JPanel formPanel = new JPanel(new BorderLayout());
        formPanel.setBackground(Color.WHITE);

        // Panel de entrada de datos
        JPanel inputPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        inputPanel.setBackground(Color.WHITE);
        TitledBorder formBorder = BorderFactory.createTitledBorder("NUEVA RECLAMACIÓN");
        formBorder.setTitleFont(LABEL_FONT);
        formBorder.setTitleColor(HEADER_COLOR);
        inputPanel.setBorder(formBorder);

        // Campos del formulario
        JLabel cedulaLabel = new JLabel("Cédula Cliente:");
        cedulaLabel.setFont(LABEL_FONT);
        JTextField cedulaField = new JTextField(15);

        JLabel mensajeLabel = new JLabel("Mensaje:");
        mensajeLabel.setFont(LABEL_FONT);
        JTextField mensajeField = new JTextField(15);

        JLabel fechaLabel = new JLabel("Fecha:");
        fechaLabel.setFont(LABEL_FONT);
        JTextField fechaField = new JTextField(15);

        // Añadir componentes al panel de entrada
        inputPanel.add(cedulaLabel);
        inputPanel.add(cedulaField);
        inputPanel.add(mensajeLabel);
        inputPanel.add(mensajeField);
        inputPanel.add(fechaLabel);
        inputPanel.add(fechaField);

        // Botón de agregar estilizado
        JButton addButton = createStyledButton("Agregar", BUTTON_COLOR);
        JPanel addButtonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        addButtonPanel.setBackground(Color.WHITE);
        addButtonPanel.add(addButton);

        JPanel formContentPanel = new JPanel(new BorderLayout());
        formContentPanel.setBackground(Color.WHITE);
        formContentPanel.add(inputPanel, BorderLayout.CENTER);
        formContentPanel.add(addButtonPanel, BorderLayout.SOUTH);

        formPanel.add(formContentPanel, BorderLayout.CENTER);

        // Panel de botones de estado
        JPanel stateButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        stateButtonPanel.setBackground(Color.WHITE);

        JButton inProgressButton = createStyledButton("Marcar como En Proceso", BUTTON_COLOR);
        JButton settledButton = createStyledButton("Marcar como Resuelta", BUTTON_COLOR);

        stateButtonPanel.add(inProgressButton);
        stateButtonPanel.add(settledButton);

        formPanel.add(stateButtonPanel, BorderLayout.SOUTH);
        mainPanel.add(formPanel, BorderLayout.SOUTH);

        setContentPane(mainPanel);

        // Load data from DB
        cargarReclamaciones();

        // Add button action
        addButton.addActionListener(e -> {
            String cedula = cedulaField.getText().trim();
            String mensaje = mensajeField.getText().trim();
            String fecha = fechaField.getText().trim();

            if (cedula.isEmpty() || mensaje.isEmpty() || fecha.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Todos los campos son obligatorios.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (!clienteDAO.existeCliente(cedula)) {
                JOptionPane.showMessageDialog(this, "La cédula no corresponde a ningún cliente registrado.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String id = UUID.randomUUID().toString(); // Generate unique ID
            Reclamacion reclamacion = new Reclamacion(id, cedula, mensaje, fecha, "Pendiente", null);
            reclamacionDAO.agregarReclamacion(reclamacion);
            tableModel.addRow(new Object[]{id, cedula, fecha, "Pendiente", mensaje});
            cedulaField.setText("");
            mensajeField.setText("");
            fechaField.setText("");
        });

        // Mark as in progress
        inProgressButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Seleccione una reclamación.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            String id = (String) tableModel.getValueAt(selectedRow, 0);
            String currentState = (String) tableModel.getValueAt(selectedRow, 3);
            if ("Pendiente".equals(currentState)) {
                reclamacionDAO.actualizarEstadoYRespuesta(id, "En Proceso", null);
                tableModel.setValueAt("En Proceso", selectedRow, 3);
            } else {
                JOptionPane.showMessageDialog(this, "Solo se puede pasar de 'Pendiente' a 'En Proceso'.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Mark as settled
        settledButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Seleccione una reclamación.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            String id = (String) tableModel.getValueAt(selectedRow, 0);
            String currentState = (String) tableModel.getValueAt(selectedRow, 3);
            if ("En Proceso".equals(currentState)) {
                String respuesta = JOptionPane.showInputDialog(this, "Ingrese la respuesta para esta reclamación:");
                if (respuesta != null && !respuesta.trim().isEmpty()) {
                    reclamacionDAO.actualizarEstadoYRespuesta(id, "Resuelta", respuesta);
                    tableModel.setValueAt("Resuelta", selectedRow, 3);
                } else {
                    JOptionPane.showMessageDialog(this, "La respuesta no puede estar vacía.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Solo se puede pasar de 'En Proceso' a 'Resuelta'.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    private JButton createStyledButton(String text, Color backgroundColor) {
        JButton button = new JButton(text);
        button.setBackground(backgroundColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setFont(new Font("Arial", Font.BOLD, 12));
        return button;
    }

    private void cargarReclamaciones() {
        List<Reclamacion> reclamaciones = reclamacionDAO.obtenerReclamaciones();
        for (Reclamacion reclamacion : reclamaciones) {
            tableModel.addRow(new Object[]{
                    reclamacion.getId(),
                    reclamacion.getCedulaCliente(),
                    reclamacion.getFecha(),
                    reclamacion.getEstado(),
                    reclamacion.getMensaje()
            });
        }
    }
}