package com.example.backend.animal.adoption.events;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class WalkTomorrowEvent extends ApplicationEvent {
    private final String email;
    private final String animalName;
    private final String time;

    public WalkTomorrowEvent(Object source, String email, String animalName, String time) {
        super(source);
        this.email = email;
        this.animalName = animalName;
        this.time = time;
    }
}
