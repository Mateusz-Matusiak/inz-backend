package com.example.backend.animal.adoption.events;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Getter
public class SurveyDecidedEvent extends ApplicationEvent {

    @Email
    private final String email;
    @NotNull
    private final Boolean isAccepted;

    public SurveyDecidedEvent(Object source, String email, Boolean isAccepted) {
        super(source);
        this.email = email;
        this.isAccepted = isAccepted;
    }
}
