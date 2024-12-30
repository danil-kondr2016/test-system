package ru.danilakondr.testsystem.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import ru.danilakondr.testsystem.data.User;
import ru.danilakondr.testsystem.services.AdminService;
import ru.danilakondr.testsystem.services.UserService;

import java.util.Optional;
import java.util.function.Supplier;

@Component
public class InitAdminAccount {
    @Autowired
    private AdminService service;

    @Value("${testsystem.admin.init.password}")
    private String adminInitPassword;

    @Value("${testsystem.admin.init.email}")
    private String adminInitEmail;

    @EventListener(ApplicationReadyEvent.class)
    public void runAfterStartup() {
        service.register(adminInitEmail, adminInitPassword);
    }
}
