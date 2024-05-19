package br.demo.backend.exception;

public class SameUsernameException extends RuntimeException{
    public SameUsernameException() {
        super("You can't change your username to your own username");
    }

}
