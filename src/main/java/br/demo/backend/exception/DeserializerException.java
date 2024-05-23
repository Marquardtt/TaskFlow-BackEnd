package br.demo.backend.exception;

public class DeserializerException extends RuntimeException{
    public DeserializerException(String message) {
        super(message);
    }
}
