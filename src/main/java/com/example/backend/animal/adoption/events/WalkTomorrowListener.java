package com.example.backend.animal.adoption.events;

import com.example.backend.mail.CustomMailSenderListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
public class WalkTomorrowListener extends CustomMailSenderListener<WalkTomorrowEvent> {

    private static final String MESSAGE = """
            Hi!
            We would like to remind you that you have scheduled walk for tomorrow with %s at %s.
            Please delete walk if you cannot make it, otherwise we can't wait to see you!
            Kind regards,
            Friendly shelter team
            """;

    protected WalkTomorrowListener(JavaMailSender mailSender, @Value("${spring.mail.username}") String sendFrom) {
        super(mailSender, sendFrom);
    }

    @Override
    public String getSendTo(WalkTomorrowEvent event) {
        return event.getEmail();
    }

    @Override
    public String getMailSubject() {
        return "Walk with animal scheduled for tomorrow!";
    }

    @Override
    public String getMailMessage(WalkTomorrowEvent event) {
        final String[] split = event.getTime().split(":");
        final String time = split[0].concat(":").concat(split[1]);
        return MESSAGE.formatted(event.getAnimalName(), time);
    }
}
