package com.UAIC.ISMA.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    @Autowired
    private JavaMailSender mailSender;

    public void sendEmail(String to, String subject, String text){
        SimpleMailMessage message = new SimpleMailMessage();

        message.setFrom("mail");
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);

        mailSender.send(message);
        System.out.println("Mail sent successfully");

        /* Urmeaza asta in controller-ul de autentificare
        emailService.sendEmail(
                     user.getEmail(),
                    "Confirmare cont",
                    "Bine ai venit, " + user.getFirstName() + "! Contul tău a fost creat cu succes."
); */
    }
}
