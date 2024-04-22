package br.demo.backend.exception;

public class TaskAlreadyDeletedException extends RuntimeException{
    public TaskAlreadyDeletedException() {
        super("This task is already deleted.");
    }
}
