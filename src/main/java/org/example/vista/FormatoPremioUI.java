package org.example.vista;

import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.example.dao.BoletoDAO;
import org.example.dao.ClienteDAO;
import org.example.dao.GanadorDAO;
import org.example.utils.EmailService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;

public class    FormatoPremioUI extends JFrame {
    private JTable tablaBoletos;
    private JTable tablaGanadores;
    private DefaultTableModel modeloBoletos;
    private DefaultTableModel modeloGanadores;
    private BoletoDAO boletoDAO;
    private GanadorDAO ganadorDAO;
    private JButton btnVolver;
    private JButton btnSeleccionarGanador;
    private JButton btnRegistrarGanador;

    // Colores para la interfaz
    private static final Color HEADER_COLOR = new Color(53, 73, 94);
    private static final Color TABLE_ALTERNATE_COLOR = new Color(240, 240, 240);
    private static final Color BACKGROUND_COLOR = new Color(245, 247, 250);
    private static final Color BUTTON_COLOR = new Color(41, 128, 185);
    private static final Color TEXT_COLOR = new Color(44, 62, 80);
    private static final Color BUTTON_TEXT_COLOR = Color.WHITE;

    // Formato para números sin notación científica
    private static final NumberFormat numberFormat = new DecimalFormat("#,###.##");

    public FormatoPremioUI(MongoDatabase database) {
        this.boletoDAO = new BoletoDAO(database);
        this.ganadorDAO = new GanadorDAO(database);

        // Configuración básica de la ventana
        setTitle("Formato de Premios");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Configurar el panel principal
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBackground(BACKGROUND_COLOR);
        mainPanel.setBorder(new EmptyBorder(15, 15, 15, 15));

        // Panel del título
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        titlePanel.setBackground(BACKGROUND_COLOR);

        JLabel titleLabel = new JLabel("Gestión de Premios");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(TEXT_COLOR);
        titlePanel.add(titleLabel);

        // Panel de tablas
        JPanel panelTablas = new JPanel(new GridLayout(2, 1, 10, 10));
        panelTablas.setBackground(BACKGROUND_COLOR);

        // Tabla de boletos
        modeloBoletos = new DefaultTableModel(new String[]{"ID", "Código Boleto", "Cédula Cliente", "Sorteo"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Hacer la tabla no editable
            }
        };
        tablaBoletos = new JTable(modeloBoletos);
        configurarTabla(tablaBoletos);
        JScrollPane scrollBoletos = new JScrollPane(tablaBoletos);
        scrollBoletos.setBorder(BorderFactory.createLineBorder(HEADER_COLOR, 1));
        panelTablas.add(scrollBoletos);

        // Tabla de ganadores
        modeloGanadores = new DefaultTableModel(new String[]{"ID", "Cédula Cliente", "Ganador", "Cantidad Ganada"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Hacer la tabla no editable
            }

            @Override
            public Class<?> getColumnClass(int columnIndex) {
                // Especificar que la columna 3 (índice) contiene valores Double
                if (columnIndex == 3) {
                    return Double.class;
                }
                return super.getColumnClass(columnIndex);
            }
        };
        tablaGanadores = new JTable(modeloGanadores);
        configurarTabla(tablaGanadores);

        // Configurar renderizador específico para la columna de cantidad ganada
        tablaGanadores.getColumnModel().getColumn(3).setCellRenderer(new NumberRenderer());

        JScrollPane scrollGanadores = new JScrollPane(tablaGanadores);
        scrollGanadores.setBorder(BorderFactory.createLineBorder(HEADER_COLOR, 1));
        panelTablas.add(scrollGanadores);

        // Panel de botones
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        panelBotones.setBackground(BACKGROUND_COLOR);

        btnSeleccionarGanador = new JButton("Seleccionar Ganador");
        configurarBoton(btnSeleccionarGanador);
        btnSeleccionarGanador.addActionListener(e -> seleccionarGanador());

        btnRegistrarGanador = new JButton("Registrar Ganador");
        configurarBoton(btnRegistrarGanador);
        btnRegistrarGanador.addActionListener(e -> registrarGanador());

        btnVolver = new JButton("Volver");
        configurarBoton(btnVolver);
        btnVolver.addActionListener(e -> {
            RegistroClienteUI registroClienteUI = new RegistroClienteUI();
            registroClienteUI.setVisible(true);
            this.dispose();
        });
// Inside the panelBotones section of FormatoPremioUI
JButton btnReclamaciones = new JButton("Reclamaciones");
configurarBoton(btnReclamaciones);
btnReclamaciones.addActionListener(e -> {
    ReclamacionesUI reclamacionesUI = new ReclamacionesUI();
    reclamacionesUI.setVisible(true);
});

panelBotones.add(btnReclamaciones);
        panelBotones.add(btnSeleccionarGanador);
        panelBotones.add(btnRegistrarGanador);
        panelBotones.add(btnVolver);

        // Añadir componentes al panel principal
        mainPanel.add(titlePanel, BorderLayout.NORTH);
        mainPanel.add(panelTablas, BorderLayout.CENTER);
        mainPanel.add(panelBotones, BorderLayout.SOUTH);

        // Establecer el panel principal como contenido
        setContentPane(mainPanel);

        // Asegurar que el Look and Feel no sobrescriba nuestros colores
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
            SwingUtilities.updateComponentTreeUI(this);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Cargar datos
        cargarBoletos();
        cargarGanadores();
    }

    // Clase para formatear números sin notación científica
    private class NumberRenderer extends DefaultTableCellRenderer {
        public NumberRenderer() {
            super();
            setHorizontalAlignment(SwingConstants.RIGHT);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus,
                                                       int row, int column) {
            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

            if (value instanceof Number) {
                setText(numberFormat.format(value));

                if (!isSelected) {
                    setBackground(row % 2 == 0 ? Color.WHITE : TABLE_ALTERNATE_COLOR);
                    setForeground(TEXT_COLOR);
                }
            }
            return c;
        }
    }

    private void configurarTabla(JTable tabla) {
        tabla.setRowHeight(30);
        tabla.setFont(new Font("Arial", Font.PLAIN, 14));
        tabla.setGridColor(new Color(220, 220, 220));
        tabla.setSelectionBackground(new Color(108, 142, 191));
        tabla.setSelectionForeground(Color.WHITE);
        tabla.setFillsViewportHeight(true);

        JTableHeader header = tabla.getTableHeader();
        header.setFont(new Font("Arial", Font.BOLD, 14));
        header.setBackground(HEADER_COLOR);
        header.setForeground(Color.WHITE);
        header.setPreferredSize(new Dimension(header.getWidth(), 35));
        header.setReorderingAllowed(false);

        tabla.setDefaultRenderer(Object.class, new javax.swing.table.DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component component = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (!isSelected) {
                    component.setBackground(row % 2 == 0 ? Color.WHITE : TABLE_ALTERNATE_COLOR);
                    component.setForeground(TEXT_COLOR);
                }
                return component;
            }
        });
    }

    private void configurarBoton(JButton boton) {
        boton.setFont(new Font("Arial", Font.BOLD, 14));
        boton.setPreferredSize(new Dimension(180, 40));
        boton.setBackground(BUTTON_COLOR);
        boton.setForeground(BUTTON_TEXT_COLOR);
        boton.setFocusPainted(false);
        boton.setBorderPainted(false);
        boton.setOpaque(true);
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Asegurar que el botón mantenga su apariencia en diferentes plataformas
        boton.setUI(new javax.swing.plaf.basic.BasicButtonUI() {
            @Override
            public void installDefaults(AbstractButton b) {
                super.installDefaults(b);
                b.setBackground(BUTTON_COLOR);
                b.setForeground(BUTTON_TEXT_COLOR);
            }

            @Override
            public void paint(Graphics g, JComponent c) {
                AbstractButton b = (AbstractButton) c;
                ButtonModel model = b.getModel();

                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                if (model.isPressed()) {
                    g2.setColor(BUTTON_COLOR.darker());
                } else if (model.isRollover()) {
                    g2.setColor(BUTTON_COLOR.brighter());
                } else {
                    g2.setColor(BUTTON_COLOR);
                }

                g2.fillRoundRect(0, 0, c.getWidth(), c.getHeight(), 10, 10);

                super.paint(g, c);
            }
        });

        // Añadir efecto hover
        boton.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                boton.setBackground(BUTTON_COLOR.brighter());
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                boton.setBackground(BUTTON_COLOR);
            }
        });
    }

    private void cargarBoletos() {
        List<Document> boletos = boletoDAO.obtenerBoletos();
        modeloBoletos.setRowCount(0); // Limpiar la tabla

        for (Document boleto : boletos) {
            modeloBoletos.addRow(new Object[]{
                    boleto.getObjectId("_id").toString(),
                    boleto.getString("codigo_boleto"),
                    boleto.getString("cedula_cliente"),
                    boleto.getString("sorteo")
            });
        }
    }

    private void cargarGanadores() {
        List<Document> ganadores = ganadorDAO.obtenerGanadores();
        modeloGanadores.setRowCount(0); // Limpiar la tabla

        for (Document ganador : ganadores) {
            modeloGanadores.addRow(new Object[]{
                    ganador.getObjectId("_id").toString(),
                    ganador.getString("cedula_cliente"),
                    "Sí",
                    ganador.getDouble("cantidad_ganada")
            });
        }
    }

    private void seleccionarGanador() {
        int selectedRow = tablaBoletos.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un boleto primero.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String cedulaCliente = (String) modeloBoletos.getValueAt(selectedRow, 2);
        JOptionPane.showMessageDialog(this, "Cliente seleccionado: " + cedulaCliente, "Información", JOptionPane.INFORMATION_MESSAGE);
    }

    private void registrarGanador() {
        String claveAdmin = JOptionPane.showInputDialog(this, "Ingrese la clave de administrador:", "Validación", JOptionPane.QUESTION_MESSAGE);
        if (!"lot01AXM".equals(claveAdmin)) {
            JOptionPane.showMessageDialog(this, "Clave incorrecta.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String cantidadGanadaStr = JOptionPane.showInputDialog(this, "Ingrese la cantidad ganada:", "Registrar Ganador", JOptionPane.QUESTION_MESSAGE);
        try {
            double cantidadGanada = Double.parseDouble(cantidadGanadaStr);

            int selectedRow = tablaBoletos.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Seleccione un boleto primero.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String cedulaCliente = (String) modeloBoletos.getValueAt(selectedRow, 2);

            // Obtener el correo del cliente desde ClienteDAO
            ClienteDAO clienteDAO = new ClienteDAO(); // Asegúrate de tener una instancia válida
            String correoCliente = clienteDAO.obtenerCorreoPorCedula(cedulaCliente);

            // Registrar en la base de datos
            ganadorDAO.registrarGanador(cedulaCliente, cantidadGanada);

            // Enviar correo al cliente
            if (correoCliente != null) {
                String asunto = "¡Felicidades! Has ganado un premio";
                String mensaje = "Estimado cliente,\n\n" +
                        "Nos complace informarte que has ganado un premio de " + cantidadGanada + " en nuestro sorteo.\n\n" +
                        "¡Gracias por participar!\n\n" +
                        "Atentamente,\nEl equipo de la lotería.";
                EmailService.enviarCorreo(correoCliente, asunto, mensaje);
            } else {
                JOptionPane.showMessageDialog(this, "No se encontró el correo del cliente.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            }

            // Actualizar la tabla de ganadores
            cargarGanadores();

            JOptionPane.showMessageDialog(this, "Ganador registrado exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Cantidad inválida.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}