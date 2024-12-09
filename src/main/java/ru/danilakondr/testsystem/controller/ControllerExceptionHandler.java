package ru.danilakondr.testsystem.controller;

  import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
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
    @ExceptionHandler
    public ResponseEntity<?> handleInvalidCredentialsException(InvalidCredentialsException e, WebRequest req) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Response.Error.INVALID_CREDENTIALS);
    }

    @ExceptionHandler
    public ResponseEntity<?> handleUsernameNotFoundException(UsernameNotFoundException e, WebRequest req) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Response.Error.INVALID_CREDENTIALS);
    }

    @ExceptionHandler
    public ResponseEntity<?> handleIllegalArgumentException(IllegalArgumentException e, WebRequest req) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Response.Error.INVALID_REQUEST);
    }

    public ResponseEntity<?> handleEntityNotFoundException(EntityNotFoundException e, WebRequest req) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Response.Error.RESOURCE_NOT_FOUND);
    }
}
