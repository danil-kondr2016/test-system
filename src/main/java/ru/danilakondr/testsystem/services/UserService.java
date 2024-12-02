package ru.danilakondr.testsystem.services;

import org.springframework.security.core.userdetails.UserDetailsService;
import ru.danilakondr.testsystem.data.Test;
import ru.danilakondr.testsystem.data.TestSession;
import ru.danilakondr.testsystem.data.User;
import ru.danilakondr.testsystem.data.UserSession;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserService extends UserDetailsService {
    User register(String login, String email, String password);
    Optional<User> find(String login);
    boolean validate(User user, String password);
    void changePassword(User user, String password);
    UserSession login(String login, String password);
    Optional<UserSession> authenticate(UserSession session);
    Optional<UserSession> authenticate(UUID sessionId);
    void logout(UserSession session);

    List<Test> getTests(User user);
    List<TestSession> getTestSessions(User user);
}
