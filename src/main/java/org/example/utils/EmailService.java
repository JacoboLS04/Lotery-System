package org.example.utils;

import javax.mail.*;
import javax.mail.internet.*;
import java.util.Properties;

public class EmailService {
    public static void enviarCorreo(String destinatario, String asunto, String mensaje) {
        // Configuración del servidor SMTP
        String remitente = "jacohola2522@gmail.com";
        String clave = "kqlb otlq bkfm brwv";

        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(remitente, clave);
            }
        });

        try {
            // Crear el mensaje
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(remitente));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(destinatario));
            message.setSubject(asunto);
            message.setText(mensaje);

            // Enviar el mensaje
            Transport.send(message);
            System.out.println("Correo enviado exitosamente a " + destinatario);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}