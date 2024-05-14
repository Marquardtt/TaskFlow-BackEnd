package br.demo.backend.exception;

public class ChatAlreadyExistsException extends RuntimeException{
    public ChatAlreadyExistsException(){
        super("The chat that the user is trying to create already exists!");
    }

}
