package me.jobayeralmahmud.gateway.exceptions;

public class BearerTokenException extends RuntimeException {
    public BearerTokenException(String message) {
        super(message);
    }
}