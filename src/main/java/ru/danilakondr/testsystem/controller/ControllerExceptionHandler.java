package ru.danilakondr.testsystem.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import ru.danilakondr.testsystem.exception.InvalidCredentialsException;
import ru.danilakondr.testsystem.protocol.Response;

@ControllerAdvice
public class ControllerExceptionHandler extends ResponseEntityExceptionHandler {
    private static final Response.Error INVALID_CREDENTIALS = new Response.Error("INVALID_CREDENTIALS");
    private static final Response.Error INVALID_REQUEST = new Response.Error("INVALID_REQUEST");

    @ExceptionHandler
    public ResponseEntity<?> handleInvalidCredentialsException(InvalidCredentialsException e, WebRequest req) {
        return ResponseEntity.status(403).body(INVALID_CREDENTIALS);
    }

    @ExceptionHandler
    public ResponseEntity<?> handleUsernameNotFoundException(UsernameNotFoundException e, WebRequest req) {
        return ResponseEntity.status(403).body(INVALID_CREDENTIALS);
    }

    @ExceptionHandler
    public ResponseEntity<?> handleIllegalArgumentException(IllegalArgumentException e, WebRequest req) {
        return ResponseEntity.status(400).body(INVALID_REQUEST);
    }
}
