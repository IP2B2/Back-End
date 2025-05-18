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
        String url = "https://ismauaic.com/reset-password?token=" + token;
        String subject = "Seteaza-ti o parola noua.";
        String message = "Hello!\n\nContul tau a fost creat, dar nu este verificat inca.\n\n" +
                "Pentru a se activa contul, este nevoie sa iti setezi o parola, accesand acest link:\n" + url +
                "\n\nLinkul expira in 60 de minute.";

        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(to);
        email.setFrom("ISMA Registration <tudor.leonte10@gmail.com>");
        email.setReplyTo("resetpassword@ismauaic.com");
        email.setSubject(subject);
        email.setText(message);

        mailSender.send(email);
    }
}
