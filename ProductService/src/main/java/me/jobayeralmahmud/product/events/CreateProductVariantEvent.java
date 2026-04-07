package me.jobayeralmahmud.product.events;

import org.springframework.context.ApplicationEvent;

public class CreateProductVariantEvent extends ApplicationEvent {
    public CreateProductVariantEvent(Object source) {
        super(source);
    }
}