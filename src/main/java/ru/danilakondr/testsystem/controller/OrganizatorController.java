package ru.danilakondr.testsystem.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.danilakondr.testsystem.data.User;
import ru.danilakondr.testsystem.data.UserSession;
import ru.danilakondr.testsystem.exception.InvalidCredentialsException;
import ru.danilakondr.testsystem.protocol.*;
import ru.danilakondr.testsystem.services.UserService;

@Controller
@RequiredArgsConstructor
public class OrganizatorController {
    private final UserService userService;

    @PostMapping("/api/login")
    public ResponseEntity<Response> authorize(@RequestBody AuthorizeRequest req) {
        UserSession session = userService.login(req.getLogin(), req.getPassword());
        return ResponseEntity.ok(new SessionKeyResponse(session.getSessionId().toString()));
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

    @PostMapping("/api/changePassword")
    public ResponseEntity<Response> changePassword(@RequestBody ChangePasswordRequest request) {
        final Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        final UserSession principal = (UserSession) auth.getPrincipal();
        final User user = principal.getUser();

        userService.changePassword(user, request.getNewPassword());
        return ResponseEntity.status(204).body(null);
    }

    @GetMapping("/api/logout")
    public ResponseEntity<Response> logout()
    {
        final Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        userService.logout((UserSession) auth.getPrincipal());
        return ResponseEntity.status(204).body(null);
    }

    // TODO implement sending of emails from the application to reset password
}