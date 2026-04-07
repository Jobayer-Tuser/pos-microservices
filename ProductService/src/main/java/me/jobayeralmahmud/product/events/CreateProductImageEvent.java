package me.jobayeralmahmud.product.events;

import org.springframework.context.ApplicationEvent;

public class CreateProductImageEvent extends ApplicationEvent {
    public CreateProductImageEvent(Object source) {
        super(source);
    }
}