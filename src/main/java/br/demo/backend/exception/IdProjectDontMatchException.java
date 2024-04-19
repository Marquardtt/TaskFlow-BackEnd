package br.demo.backend.exception;

public class IdProjectDontMatchException extends RuntimeException{
    public IdProjectDontMatchException() {
        super("The id of the project doesn't match with the id of the project in the path.");
    }
}
