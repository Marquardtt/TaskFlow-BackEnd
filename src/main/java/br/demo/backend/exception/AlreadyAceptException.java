package br.demo.backend.exception;

public class AlreadyAceptException extends RuntimeException{
    public AlreadyAceptException() {
        super("The user has already accepted the invitation.");
    }
}
