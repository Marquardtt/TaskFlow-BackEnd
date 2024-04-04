package br.demo.backend.controller;

import br.demo.backend.service.ErrorLoggerService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@AllArgsConstructor
public class ErrorHandler {

    private final ErrorLoggerService exceptionLogger;

    @ExceptionHandler(Exception.class)
    public void handleException(Exception exception) {
        exceptionLogger.logException(exception);
    }

}
