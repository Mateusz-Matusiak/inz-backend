package com.example.backend.mail;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
public class RegistrationListener extends CustomMailSenderListener<RegistrationEvent> {

    private static final String MESSAGE = """
            Hello %s
            You have registered your account on Friendly Shelter, before being able to sign in you need to verify that this is your email address by clicking here:
            %s
            Kind Regards
            """;


    protected RegistrationListener(JavaMailSender mailSender, @Value("${spring.mail.username}") String sendFrom) {
        super(mailSender, sendFrom);
    }

    @Override
    public String getSendTo(RegistrationEvent event) {
        return event.getUserToken().email();
    }

    @Override
    public String getMailSubject() {
        return "Confirm your email";
    }

    @Override
    public String getMailMessage(RegistrationEvent event) {
        return MESSAGE.formatted(event.getUserToken().email(), event.getUserToken().callbackUrl() + "?token=" + event.getUserToken().token());
    }
}
