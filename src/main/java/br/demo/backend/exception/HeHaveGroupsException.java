package br.demo.backend.exception;

public class HeHaveGroupsException extends RuntimeException {
    public HeHaveGroupsException() {
        super("There are groups where the user is owner. You can't delete this user.");
    }
}
