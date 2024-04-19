package br.demo.backend.exception;

public class TaskAlreadyCompleteException extends RuntimeException {
    public TaskAlreadyCompleteException() {
        super("This task is already complete.");
    }
}
