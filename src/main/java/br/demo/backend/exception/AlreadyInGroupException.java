package br.demo.backend.exception;

public class AlreadyInGroupException extends RuntimeException{
    public AlreadyInGroupException() {
        super("This user already are in a project that this group is in.");
    }
}
