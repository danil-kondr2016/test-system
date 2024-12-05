package ru.danilakondr.testsystem.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.danilakondr.testsystem.data.PasswordResetToken;
import ru.danilakondr.testsystem.data.User;
import ru.danilakondr.testsystem.data.UserSession;
import ru.danilakondr.testsystem.protocol.*;
import ru.danilakondr.testsystem.services.UserService;

@Controller
@RequiredArgsConstructor
public class OrganizatorController {
    private final UserService userService;

    private final JavaMailSender emailSender;

    @Value("${spring.mail.username}")
    private String from;

    @PostMapping("/api/login")
    public ResponseEntity<Response> authorize(@RequestBody AuthorizeRequest req) {
        UserSession session = userService.login(req.getLogin(), req.getPassword());
        return ResponseEntity.ok(new SessionKeyResponse(session.getSessionId().toString()));
    }

    @PostMapping("/api/register")
    public ResponseEntity<Response> register(@RequestBody RegisterRequest request) {
        try {
            userService.register(request.getLogin(), request.getEmail(), request.getPassword());
            return ResponseEntity.noContent().build();
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
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/api/logout")
    public ResponseEntity<Response> logout()
    {
        final Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        final UserSession session = (UserSession) auth.getPrincipal();
        userService.logout(session.getSessionId());
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/api/requestPasswordReset")
    public ResponseEntity<Response> requestPasswordReset(@RequestBody PasswordResetKeyRequest request) {
        PasswordResetToken token = userService.requestPasswordReset(request.getEmail());

        String messageText = "Высылаем ключ восстановления пароля:\n\n" +
                token.getResetTokenId().toString() + "\n\n" +
                "Ключ будет действовать до: " + token.getExpires().toString();

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setTo(request.getEmail());
        message.setSubject("Восстановление пароля");
        message.setText(messageText);
        emailSender.send(message);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/api/resetPassword")
    public ResponseEntity<Response> resetPassword(@RequestBody PasswordResetRequest request) {
        userService.resetPassword(request.getResetKey(), request.getNewPassword());
        return ResponseEntity.noContent().build();
    }
}