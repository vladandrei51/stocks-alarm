package com.stocks.demo.emailsender;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;

@Controller
public class MailHelper {
    @Autowired
    private JavaMailSender javaMailSender;


    public void sendEmail(String userMail, String stockName) {


        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(userMail);

        msg.setSubject(String.format("Stock %s update", stockName));
        msg.setText(String.format("The stock %s you've set the alarm for has reached its target", stockName));

        javaMailSender.send(msg);
    }
}
