package com.example.backend.animal.adoption.events;

import com.example.backend.mail.CustomMailSenderListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
public class SurveyDecidedListener extends CustomMailSenderListener<SurveyDecidedEvent> {

    private static final String ACCEPTED_MESSAGE = """
            Hi!
            Congratulations, your adoption survey was accepted!
            You can come as soon as you can and take animal to your home!
            Kind regards,
            Friendly shelter team
            """;

    private static final String DECLINED_MESSAGE = """
            Hi,
            Unfortunately your adoption survey wasn't accepted for more information check your adoption surveys in Profile tab.
            Maybe next time,
            Kind regards
            Friendly shelter team
            """;


    protected SurveyDecidedListener(JavaMailSender mailSender, @Value("${spring.mail.username}") String sendFrom) {
        super(mailSender, sendFrom);
    }

    @Override
    public String getSendTo(SurveyDecidedEvent event) {
        return event.getEmail();
    }

    @Override
    public String getMailSubject() {
        return "Your survey is decided!";
    }

    @Override
    public String getMailMessage(SurveyDecidedEvent event) {
        return event.getIsAccepted() ? ACCEPTED_MESSAGE : DECLINED_MESSAGE;
    }
}
