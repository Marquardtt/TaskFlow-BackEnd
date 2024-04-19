package br.demo.backend.exception;

public class SomeUserAlreadyIsInProjectException extends RuntimeException{
    public SomeUserAlreadyIsInProjectException() {
        super("This user already are in a project that this group is in.");
    }
}
