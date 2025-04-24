package org.example.vista;

import org.bson.Document;
import org.example.dao.BoletoDAO;
import org.example.dao.ClienteDAO;
import org.example.modelo.Cliente;
import org.example.utils.ExportUtils;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.util.List;

public class CompraBoletosUI extends JFrame {
    private JTable tablaClientes;
    private JTable tablaBoletos;
    private DefaultTableModel modeloClientes;
    private DefaultTableModel modeloBoletos;
    private JComboBox<String> comboSorteos;
    private JButton btnAsignarSorteo;
    private JButton btnVolver;
    private BoletoDAO boletoDAO;
    private JButton btnSimularPago;


    // Colores para la interfaz
    private static final Color HEADER_COLOR = new Color(53, 73, 94);
    private static final Color TABLE_ALTERNATE_COLOR = new Color(240, 240, 240);
    private static final Color BACKGROUND_COLOR = new Color(245, 247, 250);
    private static final Color BUTTON_COLOR = new Color(41, 128, 185);
    private static final Color TEXT_COLOR = new Color(44, 62, 80);
    private static final Color PANEL_TITLE_COLOR = new Color(53, 73, 94);
    private static final Color BORDER_COLOR = new Color(200, 200, 200);

    public CompraBoletosUI(BoletoDAO boletoDAO) {
        this.boletoDAO = boletoDAO;

        // Configuración básica de la ventana
        setTitle("Compra de Boletos - Lotería del Quindío");
        setSize(900, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Establecer look and feel para mejorar la apariencia
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
            SwingUtilities.updateComponentTreeUI(this);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Panel principal con espacio para márgenes
        JPanel panelPrincipal = new JPanel(new BorderLayout(15, 15));
        panelPrincipal.setBackground(BACKGROUND_COLOR);
        panelPrincipal.setBorder(new EmptyBorder(15, 15, 15, 15));
        setContentPane(panelPrincipal);

        // Panel superior: Tabla de clientes validados con diseño mejorado
        JPanel panelClientes = new JPanel(new BorderLayout(5, 5));
        TitledBorder clientesBorder = BorderFactory.createTitledBorder("Clientes Validados");
        clientesBorder.setTitleColor(PANEL_TITLE_COLOR);
        clientesBorder.setTitleFont(new Font("Arial", Font.BOLD, 14));
        panelClientes.setBorder(BorderFactory.createCompoundBorder(
                clientesBorder,
                new EmptyBorder(5, 5, 5, 5)
        ));
        panelClientes.setBackground(BACKGROUND_COLOR);
        panelClientes.setPreferredSize(new Dimension(900, 220));

        // Tabla de clientes mejorada
        modeloClientes = new DefaultTableModel(new String[]{"Nombre", "Cédula", "Teléfono", "Email", "Encargado"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // No editable
            }
        };

        tablaClientes = new JTable(modeloClientes);
        tablaClientes.setRowHeight(28);
        tablaClientes.setFont(new Font("Arial", Font.PLAIN, 13));
        tablaClientes.setGridColor(BORDER_COLOR);
        tablaClientes.setSelectionBackground(new Color(91, 155, 213));
        tablaClientes.setSelectionForeground(Color.WHITE);

        // Mejorar el encabezado de la tabla
        JTableHeader headerClientes = tablaClientes.getTableHeader();
        headerClientes.setFont(new Font("Arial", Font.BOLD, 13));
        headerClientes.setBackground(HEADER_COLOR);
        headerClientes.setForeground(Color.WHITE);
        headerClientes.setPreferredSize(new Dimension(headerClientes.getWidth(), 35));

        // Configurar el desplazamiento de la tabla
        JScrollPane scrollClientes = new JScrollPane(tablaClientes);
        scrollClientes.setBorder(BorderFactory.createLineBorder(BORDER_COLOR));
        scrollClientes.getViewport().setBackground(Color.WHITE);

        panelClientes.add(scrollClientes, BorderLayout.CENTER);
        panelPrincipal.add(panelClientes, BorderLayout.NORTH);

        // Panel central: Opciones de sorteo con diseño mejorado
        JPanel panelSorteos = new JPanel();
        TitledBorder sorteosBorder = BorderFactory.createTitledBorder("Opciones de Sorteo");
        sorteosBorder.setTitleColor(PANEL_TITLE_COLOR);
        sorteosBorder.setTitleFont(new Font("Arial", Font.BOLD, 14));
        panelSorteos.setBorder(BorderFactory.createCompoundBorder(
                sorteosBorder,
                new EmptyBorder(20, 10, 20, 10)
        ));
        panelSorteos.setBackground(BACKGROUND_COLOR);

        // Usar un Layout más organizado
        panelSorteos.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Etiqueta para el combo
        JLabel lblSorteo = new JLabel("Seleccionar Sorteo:");
        lblSorteo.setFont(new Font("Arial", Font.BOLD, 13));
        lblSorteo.setForeground(TEXT_COLOR);
        gbc.gridx = 0;
        gbc.gridy = 0;
        panelSorteos.add(lblSorteo, gbc);

        // ComboBox de sorteos
        comboSorteos = new JComboBox<>(new String[]{"Premio Mayor", "Sueldazo Cafetero", "Seco de 25 Millones", "Seco de 10 Millones"});
        comboSorteos.setFont(new Font("Arial", Font.PLAIN, 13));
        comboSorteos.setPreferredSize(new Dimension(200, 30));
        gbc.gridx = 1;
        gbc.gridy = 0;
        panelSorteos.add(comboSorteos, gbc);

        // Espacio entre elementos
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        panelSorteos.add(Box.createVerticalStrut(15), gbc);

        // Panel para botones
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        buttonPanel.setBackground(BACKGROUND_COLOR);

        // Botón Asignar Sorteo
        btnAsignarSorteo = new JButton("Asignar Sorteo");
        btnAsignarSorteo.setFont(new Font("Arial", Font.BOLD, 13));
        btnAsignarSorteo.setForeground(Color.WHITE);
        btnAsignarSorteo.setBackground(BUTTON_COLOR);
        btnAsignarSorteo.setFocusPainted(false);
        btnAsignarSorteo.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnAsignarSorteo.setPreferredSize(new Dimension(150, 35));
        btnAsignarSorteo.addActionListener(e -> asignarSorteo());

        // Botón Volver
        btnVolver = new JButton("Volver");
        btnVolver.setFont(new Font("Arial", Font.BOLD, 13));
        btnVolver.setForeground(Color.WHITE);
        btnVolver.setBackground(new Color(149, 165, 166));
        btnVolver.setFocusPainted(false);
        btnVolver.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnVolver.setPreferredSize(new Dimension(120, 35));
        btnVolver.addActionListener(e -> {
            RegistroClienteUI registroClienteUI = new RegistroClienteUI();
            registroClienteUI.setVisible(true);
            this.dispose();
        });

        buttonPanel.add(btnAsignarSorteo);
        buttonPanel.add(btnVolver);

        // Botón Simular Pago
        btnSimularPago = new JButton("Simular Pago");
        btnSimularPago.setFont(new Font("Arial", Font.BOLD, 13));
        btnSimularPago.setForeground(Color.WHITE);
        btnSimularPago.setBackground(new Color(46, 139, 87));
        btnSimularPago.setFocusPainted(false);
        btnSimularPago.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnSimularPago.setPreferredSize(new Dimension(150, 35));
        btnSimularPago.addActionListener(e -> abrirSimulacionPago());

        // Importante: Inicialmente el botón de Asignar Sorteo está deshabilitado
        btnAsignarSorteo.setEnabled(false);

        buttonPanel.add(btnSimularPago);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        panelSorteos.add(buttonPanel, gbc);

        panelPrincipal.add(panelSorteos, BorderLayout.CENTER);

        // Panel inferior: Tabla de boletos generados con diseño mejorado
        JPanel panelBoletos = new JPanel(new BorderLayout(5, 5));
        TitledBorder boletosBorder = BorderFactory.createTitledBorder("Boletos Generados");
        boletosBorder.setTitleColor(PANEL_TITLE_COLOR);
        boletosBorder.setTitleFont(new Font("Arial", Font.BOLD, 14));
        panelBoletos.setBorder(BorderFactory.createCompoundBorder(
                boletosBorder,
                new EmptyBorder(5, 5, 5, 5)
        ));
        panelBoletos.setBackground(BACKGROUND_COLOR);
        panelBoletos.setPreferredSize(new Dimension(900, 200));

        // Tabla de boletos mejorada
        modeloBoletos = new DefaultTableModel(new String[]{"Boleto", "Cédula", "Sorteo"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // No editable
            }
        };

        tablaBoletos = new JTable(modeloBoletos);
        tablaBoletos.setRowHeight(28);
        tablaBoletos.setFont(new Font("Arial", Font.PLAIN, 13));
        tablaBoletos.setGridColor(BORDER_COLOR);
        tablaBoletos.setSelectionBackground(new Color(91, 155, 213));
        tablaBoletos.setSelectionForeground(Color.WHITE);

        // Mejorar el encabezado de la tabla
        JTableHeader headerBoletos = tablaBoletos.getTableHeader();
        headerBoletos.setFont(new Font("Arial", Font.BOLD, 13));
        headerBoletos.setBackground(HEADER_COLOR);
        headerBoletos.setForeground(Color.WHITE);
        headerBoletos.setPreferredSize(new Dimension(headerBoletos.getWidth(), 35));

        // Configurar el desplazamiento de la tabla
        JScrollPane scrollBoletos = new JScrollPane(tablaBoletos);
        scrollBoletos.setBorder(BorderFactory.createLineBorder(BORDER_COLOR));
        scrollBoletos.getViewport().setBackground(Color.WHITE);

        panelBoletos.add(scrollBoletos, BorderLayout.CENTER);
        panelPrincipal.add(panelBoletos, BorderLayout.SOUTH);

        // Configurar renderizadores de celdas para filas alternadas
        configureTableRenderer(tablaClientes);
        configureTableRenderer(tablaBoletos);

        // Cargar datos
        cargarClientesValidados();
        cargarBoletosGenerados(); // Cargar boletos generados al iniciar

        // Botón para exportar a Excel
        JButton btnExportarExcel = new JButton("Exportar a Excel");
        btnExportarExcel.setBackground(new Color(46, 139, 87));
        btnExportarExcel.setForeground(Color.WHITE);
        btnExportarExcel.setFont(new Font("Arial", Font.BOLD, 12));
        btnExportarExcel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnExportarExcel.addActionListener(e -> ExportUtils.exportarTablaAExcel(tablaBoletos));

        // Botón para exportar a PDF
        JButton btnExportarPDF = new JButton("Exportar a PDF");
        btnExportarPDF.setBackground(new Color(46, 139, 87));
        btnExportarPDF.setForeground(Color.WHITE);
        btnExportarPDF.setFont(new Font("Arial", Font.BOLD, 12));
        btnExportarPDF.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnExportarPDF.addActionListener(e -> ExportUtils.exportarTablaAPDF(tablaBoletos));

        // Panel para los botones de exportación
        JPanel panelExportar = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        panelExportar.setBackground(BACKGROUND_COLOR);
        panelExportar.add(btnExportarExcel);
        panelExportar.add(btnExportarPDF);

        // Agregar el panel de exportación al panel de boletos
        panelBoletos.add(panelExportar, BorderLayout.SOUTH);

        // Botón para volver a la interfaz de RegistroClienteUI
        JButton btnVolver = new JButton("Volver");
        btnVolver.setBackground(new Color(149, 165, 166)); // Color gris
        btnVolver.setForeground(Color.WHITE); // Texto blanco
        btnVolver.setFont(new Font("Arial", Font.BOLD, 12)); // Fuente
        btnVolver.setCursor(new Cursor(Cursor.HAND_CURSOR)); // Cursor de mano
        btnVolver.addActionListener(e -> {
            RegistroClienteUI registroClienteUI = new RegistroClienteUI();
            registroClienteUI.setVisible(true); // Mostrar la ventana de RegistroClienteUI
            this.dispose(); // Cerrar la ventana actual
        });

        panelExportar.add(btnVolver);
    }

    // Método para configurar el renderizador de celdas para filas alternadas
    private void configureTableRenderer(JTable table) {
        table.setDefaultRenderer(Object.class, new javax.swing.table.DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component component = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (!isSelected) {
                    component.setBackground(row % 2 == 0 ? Color.WHITE : TABLE_ALTERNATE_COLOR);
                }
                setBorder(new EmptyBorder(0, 5, 0, 5)); // Añadir padding
                return component;
            }
        });
    }

    private void cargarClientesValidados() {
        ClienteDAO clienteDAO = new ClienteDAO();
        List<Cliente> clientesValidados = clienteDAO.obtenerClientesVerificados();

        modeloClientes.setRowCount(0); // Limpiar la tabla
        for (Cliente cliente : clientesValidados) {
            modeloClientes.addRow(new Object[]{
                    cliente.getNombre(),
                    cliente.getCedula(),
                    cliente.getTelefono(),
                    cliente.getEmail(),
                    cliente.getEncargado()
            });
        }
    }

    private void abrirSimulacionPago() {
        int filaSeleccionada = tablaClientes.getSelectedRow();
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this,
                    "Seleccione un cliente de la tabla.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Abrir SimulacionPago con un callback para habilitar el botón
        SimulacionPago simulacionPago = new SimulacionPago(() -> {
            btnAsignarSorteo.setEnabled(true); // Habilitar el botón tras el pago
            JOptionPane.showMessageDialog(this,
                    "Pago procesado con éxito. Ahora puede asignar un sorteo.",
                    "Información",
                    JOptionPane.INFORMATION_MESSAGE);
        });

        // Mostrar la interfaz de SimulacionPago
        JFrame frame = new JFrame("Simulación de Pago");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setContentPane(simulacionPago);
        frame.pack();
        frame.setLocationRelativeTo(this);
        frame.setVisible(true);
    }

    private void cargarBoletosGenerados() {
        List<Document> boletos = boletoDAO.obtenerBoletos(); // Obtener boletos desde la base de datos

        modeloBoletos.setRowCount(0); // Limpiar la tabla
        for (Document boleto : boletos) {
            modeloBoletos.addRow(new Object[]{
                    boleto.getString("codigo_boleto"),
                    boleto.getString("cedula_cliente"),
                    boleto.getString("sorteo")
            });
        }
    }

    private void asignarSorteo() {
        int filaSeleccionada = tablaClientes.getSelectedRow();
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this,
                    "Seleccione un cliente de la tabla.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        String cedula = (String) modeloClientes.getValueAt(filaSeleccionada, 1);
        String sorteo = (String) comboSorteos.getSelectedItem();

        // Solicitar código de autorización con diálogo personalizado
        JPanel panelCodigo = new JPanel(new BorderLayout(10, 10));
        panelCodigo.setBorder(new EmptyBorder(10, 10, 10, 10));

        JLabel lblCodigo = new JLabel("Ingrese el código de autorización:");
        lblCodigo.setFont(new Font("Arial", Font.BOLD, 13));
        panelCodigo.add(lblCodigo, BorderLayout.NORTH);

        JPasswordField txtCodigo = new JPasswordField(15);
        txtCodigo.setFont(new Font("Arial", Font.PLAIN, 13));
        panelCodigo.add(txtCodigo, BorderLayout.CENTER);

        int resultado = JOptionPane.showConfirmDialog(this, panelCodigo,
                "Autorización", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);

        if (resultado != JOptionPane.OK_OPTION) {
            return;
        }

        String codigo = new String(txtCodigo.getPassword());
        if (!"lot01AXM".equals(codigo)) {
            JOptionPane.showMessageDialog(this,
                    "Código de autorización incorrecto.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Generar el código único del boleto
        String codigoBoleto = boletoDAO.generarCodigoBoleto(sorteo);

        // Guardar en la base de datos
        boletoDAO.guardarBoleto(codigoBoleto, cedula, sorteo);

        // Agregar a la tabla de boletos
        modeloBoletos.addRow(new Object[]{codigoBoleto, cedula, sorteo});

        // Mensaje de éxito con diseño mejorado
        JOptionPane.showMessageDialog(this,
                "Sorteo asignado correctamente.\nCódigo de boleto: " + codigoBoleto,
                "Operación Exitosa",
                JOptionPane.INFORMATION_MESSAGE);
    }
}