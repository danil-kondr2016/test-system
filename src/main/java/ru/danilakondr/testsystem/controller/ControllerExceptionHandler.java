package ru.danilakondr.testsystem.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import ru.danilakondr.testsystem.exception.InvalidCredentialsException;
import ru.danilakondr.testsystem.protocol.ErrorResponse;

@ControllerAdvice
public class ControllerExceptionHandler extends ResponseEntityExceptionHandler {
    private static final ErrorResponse INVALID_CREDENTIALS = new ErrorResponse("INVALID_CREDENTIALS");

    @ExceptionHandler
    public ResponseEntity<?> handleInvalidCredentialsException(InvalidCredentialsException e, WebRequest req) {
        return ResponseEntity.status(403).body(INVALID_CREDENTIALS);
    }

    @ExceptionHandler
    public ResponseEntity<?> handleUsernameNotFoundException(UsernameNotFoundException e, WebRequest req) {
        return ResponseEntity.status(403).body(INVALID_CREDENTIALS);
    }
}
