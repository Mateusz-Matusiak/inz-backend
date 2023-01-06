package com.example.backend.mail;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class RegistrationEvent extends ApplicationEvent {

    private final UserToken userToken;

    public RegistrationEvent(UserToken userToken) {
        super(userToken);
        this.userToken = userToken;
    }
}
