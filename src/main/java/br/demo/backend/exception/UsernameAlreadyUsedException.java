package br.demo.backend.exception;

public class UsernameAlreadyUsedException extends RuntimeException{
    public UsernameAlreadyUsedException() {
        super("Username is already in use!");
    }
}
