package ru.danilakondr.testsystem.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.danilakondr.testsystem.data.*;
import ru.danilakondr.testsystem.protocol.*;
import ru.danilakondr.testsystem.services.TestService;
import ru.danilakondr.testsystem.services.UserService;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class OrganizatorController {
    private final UserService userService;
    private final TestService testService;

    private final JavaMailSender emailSender;

    @Value("${spring.mail.username}")
    private String from;

    @PostMapping("/api/login")
    public ResponseEntity<Response> authorize(@RequestBody AuthorizeRequest req) {
        UserSession session = userService.login(req.getLogin(), req.getPassword());
        return ResponseEntity.ok(new Response.SessionKey(session.getId().toString()));
    }

    @PostMapping("/api/register")
    public ResponseEntity<Response> register(@RequestBody RegisterRequest request) {
        try {
            userService.register(request.getLogin(), request.getEmail(), request.getPassword());
            return ResponseEntity.noContent().build();
        }
        catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Response.Error.USER_ALREADY_EXISTS);
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
        userService.logout(session.getId());
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/api/requestPasswordReset")
    public ResponseEntity<Response> requestPasswordReset(@RequestBody PasswordResetKeyRequest request) {
        PasswordResetToken token = userService.requestPasswordReset(request.getEmail());

        String messageText = "Высылаем ключ восстановления пароля:\n\n" +
                token.getId().toString() + "\n\n" +
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

    private ResponseEntity<Response> getTestsOfUser(User user) {
        List<Test> tests = testService.getByUser(user);
        List<Description> descriptions = tests.stream()
                .map(Description.Test::new)
                .collect(Collectors.toUnmodifiableList());
        Response.DescriptionList response = new Response.DescriptionList(descriptions);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/api/myTests")
    public ResponseEntity<Response> getMyTests() {
        final Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        final UserSession principal = (UserSession)auth.getPrincipal();
        return getTestsOfUser(principal.getUser());
    }

    @GetMapping("/api/tests/{username}")
    public ResponseEntity<Response> getTestsOfUser(@PathVariable("username") String username) {
        User user = userService.find(username).orElseThrow(() -> new UsernameNotFoundException(username));
        return getTestsOfUser(user);
    }
}