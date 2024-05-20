package br.demo.backend.controller;

import br.demo.backend.exception.*;
import br.demo.backend.security.exception.ForbiddenException;
import br.demo.backend.service.ErrorLoggerService;
import br.demo.backend.utils.IdProjectValidation;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.NoSuchElementException;


@ControllerAdvice
@AllArgsConstructor
public class ErrorHandler {

    private final ErrorLoggerService exceptionLogger;

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception exception) {
        exceptionLogger.logException(exception);
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleException(IllegalArgumentException exception) {
        exceptionLogger.logException(exception);
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<String> handleException(NoSuchElementException exception) {
        exceptionLogger.logException(exception);
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<String> handleException(NullPointerException exception) {
        exceptionLogger.logException(exception);
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(AlreadyInGroupException.class)
    public ResponseEntity<String> handleException(AlreadyInGroupException exception) {
        exceptionLogger.logException(exception);
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(GroupNotFoundException.class)
    public ResponseEntity<String> handleException(GroupNotFoundException exception) {
        exceptionLogger.logException(exception);
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(HeHaveGroupsException.class)
    public ResponseEntity<String> handleException(HeHaveGroupsException exception) {
        exceptionLogger.logException(exception);
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(HeHaveProjectsException.class)
    public ResponseEntity<String> handleException(HeHaveProjectsException exception) {
        exceptionLogger.logException(exception);
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(IdGroupDontMatchException.class)
    public ResponseEntity<String> handleException(IdGroupDontMatchException exception) {
        exceptionLogger.logException(exception);
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IdProjectDontMatchException.class)
    public ResponseEntity<String> handleException(IdProjectDontMatchException exception) {
        exceptionLogger.logException(exception);
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(PropertyCantBeDeletedException.class)
    public ResponseEntity<String> handleException(PropertyCantBeDeletedException exception) {
        exceptionLogger.logException(exception);
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(SomeUserAlreadyIsInProjectException.class)
    public ResponseEntity<String> handleException(SomeUserAlreadyIsInProjectException exception) {
        exceptionLogger.logException(exception);
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(TaskAlreadyCompleteException.class)
    public ResponseEntity<String> handleException(TaskAlreadyCompleteException exception) {
        exceptionLogger.logException(exception);
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(TaskAlreadyDeletedException.class)
    public ResponseEntity<String> handleException(TaskAlreadyDeletedException exception) {
        exceptionLogger.logException(exception);
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(UserCantBeAddedInThisGroupException.class)
    public ResponseEntity<String> handleException(UserCantBeAddedInThisGroupException exception) {
        exceptionLogger.logException(exception);
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(UsernameAlreadyUsedException.class)
    public ResponseEntity<String> handleException(UsernameAlreadyUsedException exception) {
        exceptionLogger.logException(exception);
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.CONFLICT);
    }
    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<String> handleException(ForbiddenException exception) {
        exceptionLogger.logException(exception);
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(DeserializerException.class)
    public ResponseEntity<String> handleException(DeserializerException exception) {
        exceptionLogger.logException(exception);
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(AlreadyAceptException.class)
    public ResponseEntity<String> handleException(AlreadyAceptException exception) {
        exceptionLogger.logException(exception);
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.CONFLICT);
    }
    @ExceptionHandler(ChatAlreadyExistsException.class)
    public ResponseEntity<String> handleException(ChatAlreadyExistsException exception) {
        exceptionLogger.logException(exception);
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(SameUsernameException.class)
    public ResponseEntity<String> handleException(SameUsernameException exception) {
        exceptionLogger.logException(exception);
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(CurrentPasswordDontMatchException.class)
    public ResponseEntity<String> handleException(CurrentPasswordDontMatchException exception) {
        exceptionLogger.logException(exception);
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.CONFLICT);
    }

}
