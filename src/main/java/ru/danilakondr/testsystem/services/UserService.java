package ru.danilakondr.testsystem.services;

import org.springframework.security.core.userdetails.UserDetailsService;
import ru.danilakondr.testsystem.data.Test;
import ru.danilakondr.testsystem.data.TestSession;
import ru.danilakondr.testsystem.data.User;

import java.util.List;
import java.util.Optional;

public interface UserService extends UserDetailsService {
    User register(String login, String email, String password);
    Optional<User> find(String login);
    boolean validate(User user, String password);
    void changePassword(User user, String password);
    List<Test> getTests(User user);
    List<TestSession> getTestSessions(User user);
}
