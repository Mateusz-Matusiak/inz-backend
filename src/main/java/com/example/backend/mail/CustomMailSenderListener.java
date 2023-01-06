package com.example.backend.mail;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.event.EventListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

public abstract class CustomMailSenderListener<T extends ApplicationEvent> {

    private final JavaMailSender mailSender;
    private final String sendFrom;

    protected CustomMailSenderListener(JavaMailSender mailSender, @Value("${spring.mail.username}") String sendFrom) {
        this.mailSender = mailSender;
        this.sendFrom = sendFrom;
    }

    @EventListener
    public void sendEmail(T event) {
        final SimpleMailMessage email = new SimpleMailMessage();
        email.setFrom(sendFrom);
        email.setTo(getSendTo(event));
        email.setSubject(getMailSubject());
        email.setText(getMailMessage(event));
        mailSender.send(email);
    }

    public abstract String getSendTo(T event);

    public abstract String getMailSubject();

    public abstract String getMailMessage(T event);
}
