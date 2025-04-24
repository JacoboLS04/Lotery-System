package org.example.vista;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.text.MaskFormatter;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import javax.swing.plaf.basic.BasicButtonUI;

public class SimulacionPago extends JPanel {
    private JTextField numeroTarjetaField;
    private JFormattedTextField fechaExpiracionField;
    private JPasswordField cvvField;
    private JButton pagarButton;
    private LoadingAnimation loadingAnimation;
    private Timer animacionTimer;
    private int anguloRotacion = 0;
    private boolean pagoCompletado = false;
    private boolean pagoRechazado = false;

    // Tarjetas de ejemplo (una válida y una inválida)
    private static final String TARJETA_VALIDA = "4111111111111111";
    private static final String TARJETA_INVALIDA = "4111111111111112";

    // Colores personalizados
    private static final Color COLOR_PRIMARIO = new Color(0, 150, 230);
    private static final Color COLOR_EXITO = new Color(46, 204, 113);
    private static final Color COLOR_ERROR = new Color(231, 76, 60);
    private static final Color COLOR_FONDO = new Color(250, 250, 250);
    private static final Color COLOR_TEXTO = new Color(50, 50, 50);
    private static final Color COLOR_BORDE = new Color(220, 220, 220);

    // Callback para notificar el éxito del pago
    private Runnable onPagoExitoso;

    public SimulacionPago(Runnable onPagoExitoso) {
        this.onPagoExitoso = onPagoExitoso;
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(30, 30, 30, 30));
        setBackground(COLOR_FONDO);

        // Panel de título
        JPanel tituloPanel = new JPanel();
        tituloPanel.setLayout(new BoxLayout(tituloPanel, BoxLayout.Y_AXIS));
        tituloPanel.setBackground(COLOR_FONDO);
        tituloPanel.setBorder(new EmptyBorder(0, 0, 20, 0));

        JLabel tituloLabel = new JLabel("Simulación de Pago");
        tituloLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        tituloLabel.setForeground(COLOR_TEXTO);
        tituloLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        tituloPanel.add(tituloLabel);

        // Panel principal que contendrá el formulario
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(COLOR_FONDO);

        // Límite de ancho para todos los componentes
        int maxWidth = 400;

        // Panel de formulario con GridBagLayout para mejor alineación
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridBagLayout());
        formPanel.setBackground(COLOR_FONDO);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 0, 15, 0);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.weightx = 1.0;

        // Número de tarjeta
        JLabel numeroTarjetaLabel = createLabel("Número de Tarjeta");
        gbc.gridy = 0;
        formPanel.add(numeroTarjetaLabel, gbc);

        numeroTarjetaField = createTextField(20);
        gbc.gridy = 1;
        gbc.insets = new Insets(0, 0, 20, 0);
        formPanel.add(numeroTarjetaField, gbc);

        // Fecha de expiración
        JLabel fechaExpiracionLabel = createLabel("Fecha de Expiración");
        gbc.gridy = 2;
        gbc.insets = new Insets(5, 0, 15, 0);
        formPanel.add(fechaExpiracionLabel, gbc);

        try {
            MaskFormatter formatter = new MaskFormatter("##/##");
            formatter.setPlaceholderCharacter('_');
            fechaExpiracionField = new JFormattedTextField(formatter);
            estilizarCampo(fechaExpiracionField);
        } catch (java.text.ParseException e) {
            fechaExpiracionField = new JFormattedTextField();
            estilizarCampo(fechaExpiracionField);
        }

        gbc.gridy = 3;
        gbc.insets = new Insets(0, 0, 20, 0);
        formPanel.add(fechaExpiracionField, gbc);

        // CVV
        JLabel cvvLabel = createLabel("CVV");
        gbc.gridy = 4;
        gbc.insets = new Insets(5, 0, 15, 0);
        formPanel.add(cvvLabel, gbc);

        cvvField = new JPasswordField(3);
        estilizarCampo(cvvField);
        gbc.gridy = 5;
        gbc.insets = new Insets(0, 0, 25, 0);
        formPanel.add(cvvField, gbc);

        // Panel para botón y animación
        JPanel actionPanel = new JPanel(new BorderLayout(0, 20));
        actionPanel.setBackground(COLOR_FONDO);
        actionPanel.setMaximumSize(new Dimension(maxWidth, 150));
        actionPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Botón de pago
        pagarButton = new JButton("Pagar");
        pagarButton.setUI(new RoundedButtonUI());
        pagarButton.setBackground(COLOR_PRIMARIO);
        pagarButton.setForeground(Color.WHITE);
        pagarButton.setFont(new Font("Segoe UI", Font.BOLD, 16));
        pagarButton.setFocusPainted(false);
        pagarButton.setBorderPainted(false);
        pagarButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        pagarButton.setPreferredSize(new Dimension(maxWidth, 50));

        // Animación de carga
        loadingAnimation = new LoadingAnimation();
        loadingAnimation.setPreferredSize(new Dimension(80, 80));
        loadingAnimation.setVisible(false);

        // Centrar animación
        JPanel animationWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER));
        animationWrapper.setBackground(COLOR_FONDO);
        animationWrapper.add(loadingAnimation);

        actionPanel.add(pagarButton, BorderLayout.NORTH);
        actionPanel.add(animationWrapper, BorderLayout.CENTER);

        // Añadir formulario al panel principal con restricciones de tamaño
        formPanel.setMaximumSize(new Dimension(maxWidth, formPanel.getPreferredSize().height));
        formPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Crear un panel centrador para mantener todo alineado
        JPanel centeringPanel = new JPanel();
        centeringPanel.setLayout(new BoxLayout(centeringPanel, BoxLayout.Y_AXIS));
        centeringPanel.setBackground(COLOR_FONDO);
        centeringPanel.add(formPanel);
        centeringPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        centeringPanel.add(actionPanel);

        // Añadir todo al panel principal con alineación centrada
        mainPanel.add(centeringPanel);

        // Añadir los paneles principales al contenedor
        add(tituloPanel, BorderLayout.NORTH);
        add(mainPanel, BorderLayout.CENTER);

        // Timer para la animación
        animacionTimer = new Timer(16, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                anguloRotacion = (anguloRotacion + 5) % 360;
                loadingAnimation.repaint();
            }
        });

        // Action listener para el botón de pago
        pagarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                procesarPago();
            }
        });
    }

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 14));
        label.setForeground(COLOR_TEXTO);
        return label;
    }

    private JTextField createTextField(int columns) {
        JTextField field = new JTextField(columns);
        estilizarCampo(field);
        return field;
    }

    private void estilizarCampo(JTextField field) {
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setPreferredSize(new Dimension(field.getPreferredSize().width, 40));
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(COLOR_BORDE, 1, true),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
    }

    private void procesarPago() {
        // Deshabilitar botón durante el procesamiento
        pagarButton.setVisible(false);
        loadingAnimation.setVisible(true);

        // Iniciar animación de procesamiento
        pagoCompletado = false;
        pagoRechazado = false;
        animacionTimer.start();

        // Simular tiempo de procesamiento (3 segundos)
        Timer procesamientoTimer = new Timer(3000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                completarProcesamiento();
                ((Timer)e.getSource()).stop();
            }
        });
        procesamientoTimer.setRepeats(false);
        procesamientoTimer.start();
    }

    private void completarProcesamiento() {
        // Validar tarjeta
        String numeroTarjeta = numeroTarjetaField.getText().replace(" ", "");

        if (numeroTarjeta.equals(TARJETA_VALIDA)) {
            pagoCompletado = true;

            // Esperar un momento para mostrar el check antes de notificar
            Timer exitoTimer = new Timer(1500, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    // Notificar éxito del pago
                    if (onPagoExitoso != null) {
                        onPagoExitoso.run();
                    }
                    ((Timer)e.getSource()).stop();
                }
            });
            exitoTimer.setRepeats(false);
            exitoTimer.start();

        } else {
            pagoRechazado = true;

            // Esperar un momento para mostrar la X y luego volver al botón
            Timer falloTimer = new Timer(1500, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    animacionTimer.stop();
                    loadingAnimation.setVisible(false);
                    pagarButton.setVisible(true);
                    pagoRechazado = false;
                    repaint();
                    ((Timer)e.getSource()).stop();
                }
            });
            falloTimer.setRepeats(false);
            falloTimer.start();
        }
    }

    // Clase para la animación de carga con círculo giratorio
    private class LoadingAnimation extends JPanel {

        public LoadingAnimation() {
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g.create();

            // Configurar antialiasing para suavizar bordes
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int width = getWidth();
            int height = getHeight();
            int size = Math.min(width, height) - 10;
            int x = (width - size) / 2;
            int y = (height - size) / 2;

            if (pagoCompletado) {
                // Dibujar círculo verde con check
                g2d.setColor(COLOR_EXITO);
                g2d.fillOval(x, y, size, size);

                // Dibujar check
                g2d.setColor(Color.WHITE);
                g2d.setStroke(new BasicStroke(size/10f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));

                int checkWidth = size / 2;
                int checkHeight = size / 3;
                int startX = x + size/4;
                int startY = y + size/2;

                g2d.drawLine(startX, startY, startX + checkWidth/3, startY + checkHeight/2);
                g2d.drawLine(startX + checkWidth/3, startY + checkHeight/2,
                        startX + checkWidth, startY - checkHeight/3);

            } else if (pagoRechazado) {
                // Dibujar círculo rojo con X
                g2d.setColor(COLOR_ERROR);
                g2d.fillOval(x, y, size, size);

                // Dibujar X
                g2d.setColor(Color.WHITE);
                g2d.setStroke(new BasicStroke(size/10f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));

                int padding = size / 4;
                g2d.drawLine(x + padding, y + padding, x + size - padding, y + size - padding);
                g2d.drawLine(x + size - padding, y + padding, x + padding, y + size - padding);

            } else {
                // Dibujar círculo giratorio (animación de carga)
                int strokeWidth = size / 12;
                int circleDiameter = size - strokeWidth * 2;
                int circleX = x + strokeWidth;
                int circleY = y + strokeWidth;

                g2d.setStroke(new BasicStroke(strokeWidth, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));

                // Dibujar círculo de fondo
                g2d.setColor(new Color(COLOR_PRIMARIO.getRed(),
                        COLOR_PRIMARIO.getGreen(),
                        COLOR_PRIMARIO.getBlue(), 30));
                g2d.drawOval(circleX, circleY, circleDiameter, circleDiameter);

                // Dibujar arco giratorio
                g2d.setColor(COLOR_PRIMARIO);
                g2d.drawArc(circleX, circleY, circleDiameter, circleDiameter,
                        anguloRotacion, 120);
            }

            g2d.dispose();
        }
    }

    // Clase para crear botones con esquinas redondeadas
    private class RoundedButtonUI extends BasicButtonUI {
        @Override
        public void paint(Graphics g, JComponent c) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            AbstractButton b = (AbstractButton) c;
            ButtonModel model = b.getModel();

            int width = b.getWidth();
            int height = b.getHeight();
            int arcRadius = 8;

            // Ajustar color si está presionado o deshabilitado
            if (model.isPressed()) {
                g2.setColor(darkenColor(b.getBackground()));
            } else if (!model.isEnabled()) {
                g2.setColor(new Color(180, 180, 180));
            } else {
                g2.setColor(b.getBackground());
            }

            // Dibujar fondo redondeado
            g2.fillRoundRect(0, 0, width, height, arcRadius, arcRadius);

            // Añadir sombra sutíl al botón
            if (model.isEnabled() && !model.isPressed()) {
                g2.setColor(new Color(0, 0, 0, 30));
                g2.fillRoundRect(0, height-4, width, 4, arcRadius, arcRadius);
            }

            g2.dispose();

            super.paint(g, c);
        }

        @Override
        protected void paintButtonPressed(Graphics g, AbstractButton b) {
            // No hacer nada aquí, lo manejamos en paint()
        }

        private Color darkenColor(Color color) {
            float[] hsb = Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), null);
            return Color.getHSBColor(hsb[0], hsb[1], Math.max(0, hsb[2] - 0.1f));
        }
    }
}