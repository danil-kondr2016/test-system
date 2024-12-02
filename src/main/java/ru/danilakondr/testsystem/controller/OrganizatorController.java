package ru.danilakondr.testsystem.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.danilakondr.testsystem.data.User;
import ru.danilakondr.testsystem.exception.InvalidCredentialsException;
import ru.danilakondr.testsystem.protocol.*;
import ru.danilakondr.testsystem.services.AuthService;
import ru.danilakondr.testsystem.services.UserService;


@Controller
@RequiredArgsConstructor
public class OrganizatorController {
    private final AuthService authService;
    private final UserService userService;

    @PostMapping("/api/login")
    public ResponseEntity<Response> authorize(@RequestBody AuthorizeRequest req) {
        try {
            final JwtResponse body = authService.login(req);
            return ResponseEntity.ok(body);
        }
        catch (InvalidCredentialsException e) {
            return ResponseEntity.status(403).body(new ErrorResponse(e.getMessage()));
        }
    }

    @PostMapping("/api/login/token")
    public ResponseEntity<Response> getNewAccessToken(@RequestBody RefreshJwtRequest request) {
        try {
            final JwtResponse token = authService.getAccessToken(request.getRefreshToken());
            return ResponseEntity.ok(token);
        }
        catch (InvalidCredentialsException e) {
            return ResponseEntity.status(403).body(new ErrorResponse(e.getMessage()));
        }
    }

    @PostMapping("/api/login/refresh")
    public ResponseEntity<Response> getNewRefreshToken(@RequestBody RefreshJwtRequest request) {
        try {
            final JwtResponse token = authService.refresh(request.getRefreshToken());
            return ResponseEntity.ok(token);
        }
        catch (InvalidCredentialsException e) {
            return ResponseEntity.status(403).body(new ErrorResponse(e.getMessage()));
        }
    }

    @PostMapping("/api/register")
    public ResponseEntity<Response> register(@RequestBody RegisterRequest request) {
        try {
            userService.register(request.getLogin(), request.getEmail(), request.getPassword());
            return ResponseEntity.status(201).body(null);
        }
        catch (IllegalArgumentException e) {
            return ResponseEntity.status(403).body(new ErrorResponse("USER_ALREADY_EXISTS"));
        }
    }
}
