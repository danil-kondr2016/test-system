package ru.danilakondr.testsystem.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import ru.danilakondr.testsystem.data.User;
import ru.danilakondr.testsystem.protocol.Response;
import ru.danilakondr.testsystem.services.AdminService;

import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class AdminController {
    private final AdminService service;

    @GetMapping("/api/admin/systemInfo")
    public ResponseEntity<Response> getSystemInfo() {
        final Optional<User> user = UserUtils.getCurrentUser();
        if (user.isEmpty())
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Response.Error.PERMISSION_DENIED);

        if (user.get().getUserRole() != User.Role.ADMINISTRATOR)
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Response.Error.PERMISSION_DENIED);

        return ResponseEntity.ok(new Response.SystemInfo(service.getSystemInfo()));
    }
}
