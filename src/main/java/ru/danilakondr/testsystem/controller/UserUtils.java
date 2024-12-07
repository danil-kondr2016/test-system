package ru.danilakondr.testsystem.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import ru.danilakondr.testsystem.data.User;
import ru.danilakondr.testsystem.data.UserSession;

public final class UserUtils {
    public static User getCurrentUser() {
        final Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        final UserSession principal = (UserSession) auth.getPrincipal();
        return principal.getUser();
    }
}
