package br.demo.backend.exception;

public class UserCantBeAddedInThisGroupException extends RuntimeException{
    public UserCantBeAddedInThisGroupException() {
        super("This user already are in a project that this group is in.");
    }
}
