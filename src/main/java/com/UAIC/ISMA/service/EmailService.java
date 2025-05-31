package com.UAIC.ISMA.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    @Autowired
    private JavaMailSender mailSender;

    public void sendActivationEmail(String to, String token) {
        String url = "https://ismauaic.com/auth/finish-register?token=" + token;
        String subject = "Completeaza cu datele tale";
        String message = "Hello!\n\nContul tau a fost creat, dar nu este verificat inca.\n\n" +
                "Pentru a se activa contul, este nevoie sa iti setezi o parola si sa oferi mai multe date, accesand acest link:\n" + url +
                "\n\nLinkul expira in 60 de minute.";

        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(to);
        email.setFrom("ISMA Registration <tudor.leonte10@gmail.com>");
        email.setReplyTo("resetpassword@ismauaic.com");
        email.setSubject(subject);
        email.setText(message);
        mailSender.send(email);
    }

    public void sendVirtualAccessCredentials(String to, String username, String password) {
        String subject = "Acces Virtual Aprobat - Resurse UAIC";

        String message = "Salut!\n\n" +
                "Cererea ta pentru acces la resurse virtuale a fost aprobată.\n\n" +
                "Ai primit următoarele date de autentificare:\n" +
                "Username: " + username + "\n" +
                "Parola: " + password + "\n\n" +
                "Te rugăm să te autentifici cât mai curând și să îți schimbi parola la prima utilizare.\n\n" +
                "Succes!\nEchipa ISMA";

        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(to);
        email.setFrom("ISMA Virtual Access <tudor.leonte10@gmail.com>");
        email.setReplyTo("support@ismauaic.com");
        email.setSubject(subject);
        email.setText(message);

        mailSender.send(email);
    }

    public void sendAccountApprovedNotification(String to) {
        String subject = "Cont aprobat - ISMA";
        String message = "Salut!\n\nContul tău a fost activat de un administrator.\n" +
                "Acum poți inchiria echipamente prin platforma ISMA, simplu si usor.\n\n" +
                "Succes!\nEchipa ISMA";

        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(to);
        email.setSubject(subject);
        email.setText(message);
        email.setFrom("ISMA Support <tudor.leonte10@gmail.com>");
        mailSender.send(email);
    }


}
