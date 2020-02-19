package com.amigoscode.demo.emailsender;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;

@Controller
public class MailHelper {
    @Autowired
    private JavaMailSender javaMailSender;


    public void sendEmail() {


        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo("vladandrei51@gmail.com");

        msg.setSubject("Testing from Spring Boot");
        msg.setText("Hello World \n Spring Boot Email");

        javaMailSender.send(msg);
        System.out.println("sending mail ---");

    }
}
