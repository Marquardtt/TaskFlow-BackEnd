package br.demo.backend.exception;

public class HeHaveProjectsException extends RuntimeException {
    public HeHaveProjectsException() {
        super("There are projects where the user is owner. You can't delete this user.");
    }
}
