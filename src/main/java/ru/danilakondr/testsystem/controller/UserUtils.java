package ru.danilakondr.testsystem.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import ru.danilakondr.testsystem.data.Participant;
import ru.danilakondr.testsystem.data.User;
import ru.danilakondr.testsystem.data.UserSession;

import java.util.Optional;

public final class UserUtils {
    public static Optional<User> getCurrentUser() {
        final Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null)
            return Optional.empty();

        final Object principal = auth.getPrincipal();
        if (principal == null)
            return Optional.empty();

        if (principal instanceof UserSession) {
            return Optional.of(((UserSession)principal).getUser());
        }
        return Optional.empty();
    }

    public static Optional<Participant> getCurrentParticipant() {
        final Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null)
            return Optional.empty();

        final Object principal = auth.getPrincipal();
        if (principal == null)
            return Optional.empty();

        if (principal instanceof Participant) {
            return Optional.of((Participant) principal);
        }
        return Optional.empty();
    }
}
