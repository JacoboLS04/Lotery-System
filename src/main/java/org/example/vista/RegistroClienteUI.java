package org.example.vista;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.util.Date;
import java.text.SimpleDateFormat;

import com.mongodb.client.MongoDatabase;
import org.example.dao.BoletoDAO;
import org.example.dao.ClienteDAO;
import org.example.dao.DatabaseConnection;
import org.example.modelo.Cliente;
import org.example.utils.ExportUtils;

import java.util.List;

public class RegistroClienteUI extends JFrame {
    // Componentes principales
    private JPanel panelPrincipal;
    private JPanel panelFormulario;
    private JPanel panelTabla;
    private JPanel panelBotones;

    // Componentes del formulario
    private JTextField campoFecha;
    private JTextField campoEncargado;
    private JTextField campoNombre;
    private JTextField campoCedula;
    private JTextField campoTelefono;
    private JTextField campoEmail;
    private JTextField campoBillete;
    private JButton btnFecha;

    // Tabla de clientes
    private JTable tablaClientes;
    private DefaultTableModel modeloTabla;
    private JScrollPane scrollTabla;

    // Botones de acción
    private JButton btnNuevo;
    private JButton btnRegistrar;
    private JButton btnSeleccionar;
    private JButton btnEliminar;
    private JButton btnVerificar;
    private JButton btnBuscar;
    private JButton btnFormatoPremio;
    private JButton btnPremio;
    private JButton btnExportarExcel;
    private JButton btnExportarPDF;

    // Colores personalizados
    private final Color COLOR_PRIMARIO = new Color(43, 78, 107);      // Color azul oscuro para encabezados
    private final Color COLOR_SECUNDARIO = new Color(108, 142, 191);   // Color azul medio para botones
    private final Color COLOR_ACENTO = new Color(108, 142, 191);        // Color verde para botones de acción positiva
    private final Color COLOR_ADVERTENCIA = new Color(187, 58, 58);   // Color rojo para botones de eliminación
    private final Color COLOR_FONDO = new Color(242, 244, 248);       // Color de fondo principal
    private final Color COLOR_TEXTO = new Color(33, 37, 41);          // Color de texto
    private final Color COLOR_TEXTO_CLARO = Color.WHITE;              // Color de texto claro

    public RegistroClienteUI() {
        // Forzar el Look and Feel para una apariencia más moderna
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        configurarVentana();
        inicializarComponentes();
        agregarComponentes();
    }

    private void configurarVentana() {
        setTitle("Sistema de Registro de Clientes - Lotería del Quindío");
        setSize(1100, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Configurar panel principal con BorderLayout
        panelPrincipal = new JPanel(new BorderLayout(10, 10));
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        panelPrincipal.setBackground(COLOR_FONDO);
        setContentPane(panelPrincipal);
    }

    private void inicializarComponentes() {
        // Panel de formulario con GridBagLayout para mejor control
        panelFormulario = new JPanel(new GridBagLayout());
        panelFormulario.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(
                        BorderFactory.createLineBorder(COLOR_PRIMARIO, 2),
                        "FORMULARIO DE REGISTRO",
                        TitledBorder.DEFAULT_JUSTIFICATION,
                        TitledBorder.DEFAULT_POSITION,
                        new Font("Arial", Font.BOLD, 12),
                        COLOR_PRIMARIO),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        panelFormulario.setBackground(COLOR_FONDO);

        // Panel para la tabla
        panelTabla = new JPanel(new BorderLayout(5, 5));
        panelTabla.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(
                        BorderFactory.createLineBorder(COLOR_PRIMARIO, 2),
                        "LISTA DE CLIENTES",
                        TitledBorder.DEFAULT_JUSTIFICATION,
                        TitledBorder.DEFAULT_POSITION,
                        new Font("Arial", Font.BOLD, 12),
                        COLOR_PRIMARIO),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        panelTabla.setBackground(COLOR_FONDO);

        // Panel para botones - usando FlowLayout para distribuir los botones uniformemente
        panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        panelBotones.setBorder(BorderFactory.createEmptyBorder(10, 5, 5, 5));
        panelBotones.setBackground(COLOR_FONDO);

        // Inicializar campos del formulario con estilo mejorado
        campoFecha = crearCampoTexto(15);
        campoFecha.setEditable(false);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        campoFecha.setText(sdf.format(new Date()));

        // Crear botón de fecha con apariencia consistente
        btnFecha = new JButton("...") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(COLOR_SECUNDARIO);
                g2.fillRect(0, 0, getWidth(), getHeight());
                super.paintComponent(g);
                g2.dispose();
            }
        };
        btnFecha.setToolTipText("Seleccionar fecha");
        btnFecha.setForeground(COLOR_TEXTO_CLARO);
        btnFecha.setFocusPainted(false);
        btnFecha.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnFecha.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(COLOR_SECUNDARIO.darker(), 1),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));

        campoEncargado = crearCampoTexto(20);
        campoNombre = crearCampoTexto(20);
        campoCedula = crearCampoTexto(15);
        campoTelefono = crearCampoTexto(15);
        campoEmail = crearCampoTexto(20);
        campoBillete = crearCampoTexto(15);

        // Inicializar la tabla con estilo mejorado
        String[] columnas = {"Nombre", "Cédula", "Teléfono", "Email", "Billete", "Verificado", "Encargado"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Hacer que la tabla no sea editable
            }
        };

        tablaClientes = new JTable(modeloTabla);
        tablaClientes.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablaClientes.setRowHeight(28);
        tablaClientes.setFillsViewportHeight(true);
        tablaClientes.getTableHeader().setReorderingAllowed(false);
        tablaClientes.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        tablaClientes.getTableHeader().setBackground(COLOR_PRIMARIO);
        tablaClientes.getTableHeader().setForeground(COLOR_TEXTO_CLARO);
        tablaClientes.setShowGrid(true);
        tablaClientes.setGridColor(new Color(220, 220, 220));
        tablaClientes.setBackground(Color.WHITE);
        tablaClientes.setFont(new Font("Arial", Font.PLAIN, 12));

        // Aplicar renderer alternando colores de filas
        tablaClientes.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

                if (isSelected) {
                    c.setBackground(COLOR_SECUNDARIO);
                    c.setForeground(COLOR_TEXTO_CLARO);
                } else {
                    c.setBackground(row % 2 == 0 ? Color.WHITE : new Color(240, 240, 240));
                    c.setForeground(COLOR_TEXTO);
                }

                ((JLabel)c).setHorizontalAlignment(column == 1 || column == 2 || column == 4 ? JLabel.CENTER : JLabel.LEFT);
                return c;
            }
        });

        tablaClientes.getColumnModel().getColumn(0).setPreferredWidth(150);
        tablaClientes.getColumnModel().getColumn(1).setPreferredWidth(100);
        tablaClientes.getColumnModel().getColumn(2).setPreferredWidth(100);
        tablaClientes.getColumnModel().getColumn(3).setPreferredWidth(150);
        tablaClientes.getColumnModel().getColumn(4).setPreferredWidth(100);

        scrollTabla = new JScrollPane(tablaClientes);
        scrollTabla.setBorder(BorderFactory.createLineBorder(COLOR_PRIMARIO));

        // Crear los botones de acción con la nueva implementación
        btnNuevo = crearBotonPersonalizado("Nuevo", "Limpiar formulario", COLOR_SECUNDARIO);
        btnRegistrar = crearBotonPersonalizado("Registrar", "Guardar cliente", COLOR_ACENTO);
        btnSeleccionar = crearBotonPersonalizado("Seleccionar", "Editar cliente seleccionado", COLOR_SECUNDARIO);
        btnEliminar = crearBotonPersonalizado("Eliminar", "Eliminar cliente seleccionado", COLOR_ADVERTENCIA);
        btnVerificar = crearBotonPersonalizado("Verificar", "Verificar datos del cliente", new Color(108, 142, 191));
        btnBuscar = crearBotonPersonalizado("Buscar", "Buscar cliente", COLOR_SECUNDARIO);

        btnFormatoPremio = crearBotonPersonalizado("Compra Boletos", "Abrir Compra de Boletos", COLOR_SECUNDARIO);
        btnFormatoPremio.addActionListener(e -> {
            MongoDatabase database = DatabaseConnection.getDatabase();
            BoletoDAO boletoDAO = new BoletoDAO(database);
            CompraBoletosUI compraBoletosUI = new CompraBoletosUI(boletoDAO);
            compraBoletosUI.setVisible(true);
            this.dispose();
        });

        btnPremio = crearBotonPersonalizado("Formato", "Abrir Formato", new Color(108, 142, 191));
        btnPremio.addActionListener(e -> {
            MongoDatabase database = DatabaseConnection.getDatabase();
            FormatoPremioUI formatoPremioUI = new FormatoPremioUI(database);
            formatoPremioUI.setVisible(true);
            this.dispose();
        });

        // Botones para exportar - con color distintivo y texto claro
        btnExportarExcel = crearBotonPersonalizado("Exportar a Excel", "Exportar datos a Excel", new Color(44, 144, 98));
        btnExportarExcel.addActionListener(e -> ExportUtils.exportarTablaAExcel(tablaClientes));

        btnExportarPDF = crearBotonPersonalizado("Exportar a PDF", "Exportar datos a PDF",  new Color(44, 144, 98));
        btnExportarPDF.addActionListener(e -> ExportUtils.exportarTablaAPDF(tablaClientes));
    }

    private JTextField crearCampoTexto(int columnas) {
        JTextField campo = new JTextField(columnas);
        campo.setFont(new Font("Arial", Font.PLAIN, 12));
        campo.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                BorderFactory.createEmptyBorder(5, 7, 5, 7)));
        return campo;
    }

    // Método mejorado para crear botones con apariencia consistente
    private JButton crearBotonPersonalizado(String texto, String tooltip, Color color) {
        JButton boton = new JButton(texto) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Relleno del botón con el color especificado
                g2.setColor(isEnabled() ?
                        (getModel().isPressed() ? color.darker() :
                                (getModel().isRollover() ? color.brighter() : color)) :
                        new Color(color.getRed(), color.getGreen(), color.getBlue(), 150));

                g2.fillRect(0, 0, getWidth(), getHeight());

                // Dibujar un borde más oscuro
                g2.setColor(color.darker());
                g2.drawRect(0, 0, getWidth() - 1, getHeight() - 1);

                g2.dispose();

                // Delegar el dibujo del texto a la implementación de superclase
                super.paintComponent(g);
            }
        };

        boton.setToolTipText(tooltip);
        boton.setForeground(COLOR_TEXTO_CLARO);
        boton.setFocusPainted(false);
        boton.setFont(new Font("Arial", Font.BOLD, 12));
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        boton.setPreferredSize(new Dimension(130, 35));

        // Hacemos que el botón sea transparente para que nuestro paintComponent maneje el renderizado
        boton.setContentAreaFilled(false);
        boton.setBorderPainted(false);
        boton.setOpaque(false);

        // Establecer un margen interior adecuado
        boton.setMargin(new Insets(6, 10, 6, 10));

        return boton;
    }

    private void agregarComponentes() {
        // Usar GridBagConstraints para un mejor posicionamiento en el formulario
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        // Agregar dirección centrada en la parte superior
        JLabel lblDireccion = new JLabel("Cra. 16 #19-21, Armenia, Quindío");
        lblDireccion.setFont(new Font("Arial", Font.ITALIC, 12));
        lblDireccion.setForeground(COLOR_SECUNDARIO);
        lblDireccion.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 4;
        panelFormulario.add(lblDireccion, gbc);

        // Primera fila - Fecha y Encargado
        gbc.gridwidth = 1;
        gbc.gridy = 1;
        gbc.gridx = 0;
        JLabel lblFecha = crearEtiqueta("Fecha:");
        panelFormulario.add(lblFecha, gbc);

        gbc.gridx = 1;
        JPanel panelFecha = new JPanel(new BorderLayout(5, 0));
        panelFecha.setBackground(COLOR_FONDO);
        panelFecha.add(campoFecha, BorderLayout.CENTER);
        panelFecha.add(btnFecha, BorderLayout.EAST);
        panelFormulario.add(panelFecha, gbc);

        gbc.gridx = 2;
        JLabel lblEncargado = crearEtiqueta("Encargado:");
        panelFormulario.add(lblEncargado, gbc);

        gbc.gridx = 3;
        panelFormulario.add(campoEncargado, gbc);

        // Sección INFORMACIÓN CLIENTE
        gbc.gridy = 2;
        gbc.gridx = 0;
        gbc.gridwidth = 4;
        JLabel lblInfoCliente = new JLabel("INFORMACIÓN DEL CLIENTE");
        lblInfoCliente.setFont(new Font("Arial", Font.BOLD, 14));
        lblInfoCliente.setForeground(COLOR_PRIMARIO);
        lblInfoCliente.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, COLOR_PRIMARIO),
                BorderFactory.createEmptyBorder(10, 0, 5, 0)
        ));
        panelFormulario.add(lblInfoCliente, gbc);

        // Nombre y Cédula
        gbc.gridy = 3;
        gbc.gridx = 0;
        gbc.gridwidth = 1;
        JLabel lblNombre = crearEtiqueta("Nombre:");
        panelFormulario.add(lblNombre, gbc);

        gbc.gridx = 1;
        panelFormulario.add(campoNombre, gbc);

        gbc.gridx = 2;
        JLabel lblCedula = crearEtiqueta("Cédula:");
        panelFormulario.add(lblCedula, gbc);

        gbc.gridx = 3;
        panelFormulario.add(campoCedula, gbc);

        // Teléfono y Email
        gbc.gridy = 4;
        gbc.gridx = 0;
        JLabel lblTelefono = crearEtiqueta("Teléfono:");
        panelFormulario.add(lblTelefono, gbc);

        gbc.gridx = 1;
        panelFormulario.add(campoTelefono, gbc);

        gbc.gridx = 2;
        JLabel lblEmail = crearEtiqueta("Email:");
        panelFormulario.add(lblEmail, gbc);

        gbc.gridx = 3;
        panelFormulario.add(campoEmail, gbc);

        // Billete y botón Buscar
        gbc.gridy = 5;
        gbc.gridx = 0;
        JLabel lblBillete = crearEtiqueta("Billete:");
        panelFormulario.add(lblBillete, gbc);

        gbc.gridx = 1;
        panelFormulario.add(campoBillete, gbc);

        gbc.gridx = 2;
        panelFormulario.add(btnBuscar, gbc);

        // Añadir tabla al panel de tabla
        panelTabla.add(scrollTabla, BorderLayout.CENTER);

        // Panel de botones en la parte inferior
        // Usamos un GridLayout para colocar los botones uniformemente en una fila
        JPanel contenedorBotones = new JPanel(new GridLayout(1, 7, 10, 0));
        contenedorBotones.setBackground(COLOR_FONDO);
        contenedorBotones.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));

        // Añadir botones al contenedor
        contenedorBotones.add(btnNuevo);
        contenedorBotones.add(btnRegistrar);
        contenedorBotones.add(btnSeleccionar);
        contenedorBotones.add(btnEliminar);
        contenedorBotones.add(btnVerificar);
        contenedorBotones.add(btnFormatoPremio);
        contenedorBotones.add(btnPremio);

        // Panel adicional para botones de exportación
        JPanel panelExportar = new JPanel(new GridLayout(1, 2, 10, 0));
        panelExportar.setBackground(COLOR_FONDO);
        panelExportar.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 10));
        panelExportar.add(btnExportarExcel);
        panelExportar.add(btnExportarPDF);

        // Panel contenedor para ambos grupos de botones
        JPanel contenedorTotalBotones = new JPanel(new BorderLayout());
        contenedorTotalBotones.setBackground(COLOR_FONDO);
        contenedorTotalBotones.add(contenedorBotones, BorderLayout.CENTER);
        contenedorTotalBotones.add(panelExportar, BorderLayout.SOUTH);

        panelBotones.add(contenedorTotalBotones);

        // Añadir paneles al panel principal
        panelPrincipal.add(panelFormulario, BorderLayout.NORTH);
        panelPrincipal.add(panelTabla, BorderLayout.CENTER);
        panelPrincipal.add(panelBotones, BorderLayout.SOUTH);
    }

    private JLabel crearEtiqueta(String texto) {
        JLabel etiqueta = new JLabel(texto);
        etiqueta.setFont(new Font("Arial", Font.BOLD, 12));
        etiqueta.setForeground(COLOR_PRIMARIO);
        return etiqueta;
    }



    // Getters para los componentes (necesarios para el controlador)
    public JTextField getCampoFecha() { return campoFecha; }
    public JTextField getCampoEncargado() { return campoEncargado; }
    public JTextField getCampoNombre() { return campoNombre; }
    public JTextField getCampoCedula() { return campoCedula; }
    public JTextField getCampoTelefono() { return campoTelefono; }
    public JTextField getCampoEmail() { return campoEmail; }
    public JTextField getCampoBillete() { return campoBillete; }

    public JButton getBtnFecha() { return btnFecha; }
    public JButton getBtnNuevo() { return btnNuevo; }
    public JButton getBtnRegistrar() { return btnRegistrar; }
    public JButton getBtnSeleccionar() { return btnSeleccionar; }
    public JButton getBtnEliminar() { return btnEliminar; }
    public JButton getBtnVerificar() { return btnVerificar; }
    public JButton getBtnBuscar() { return btnBuscar; }
    public JButton getBtnFormatoPremio() { return btnFormatoPremio; }
    public JButton getBtnPremio() { return btnPremio; }
    public JButton getBtnExportarExcel() { return btnExportarExcel; }
    public JButton getBtnExportarPDF() { return btnExportarPDF; }

    public JTable getTablaClientes() { return tablaClientes; }
    public DefaultTableModel getModeloTabla() { return modeloTabla; }

    // Métodos para actualizar la tabla
    public void cargarDatosEnTabla(List<Cliente> clientes) {
        DefaultTableModel modeloTabla = (DefaultTableModel) tablaClientes.getModel();
        modeloTabla.setRowCount(0); // Clear the table

        for (Cliente cliente : clientes) {
            modeloTabla.addRow(new Object[]{
                    cliente.getNombre(),
                    cliente.getCedula(),
                    cliente.getTelefono(),
                    cliente.getEmail(),
                    cliente.getBillete(),
                    cliente.isVerificado() ? "Sí" : "No",
                    cliente.getEncargado()
            });
        }
    }

    // Método para mostrar mensajes
    public void mostrarMensaje(String mensaje, String titulo, int tipo) {
        JOptionPane.showMessageDialog(this, mensaje, titulo, tipo);
    }

    @Override
    public void setVisible(boolean visible) {
        super.setVisible(visible);
        if (visible) {
            // Recargar los datos de la tabla desde la base de datos
            ClienteDAO clienteDAO = new ClienteDAO();
            List<Cliente> clientes = clienteDAO.obtenerTodos(); // Método que obtiene todos los clientes
            cargarDatosEnTabla(clientes); // Actualizar la tabla con los datos
        }
    }

    public String solicitarInput(String mensaje, String titulo) {
        return JOptionPane.showInputDialog(this, mensaje, titulo, JOptionPane.QUESTION_MESSAGE);
    }

    public int confirmar(String mensaje, String titulo) {
        return JOptionPane.showConfirmDialog(this, mensaje, titulo, JOptionPane.YES_NO_OPTION);
    }
}