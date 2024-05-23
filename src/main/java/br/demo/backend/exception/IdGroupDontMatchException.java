package br.demo.backend.exception;

public class IdGroupDontMatchException extends RuntimeException{
    public IdGroupDontMatchException() {
        super("The id of the group doesn't match with the id of the group in the path.");
    }
}
