package ru.danilakondr.testsystem.services;

import org.springframework.security.core.userdetails.UserDetailsService;
import ru.danilakondr.testsystem.data.Test;
import ru.danilakondr.testsystem.data.TestSession;
import ru.danilakondr.testsystem.data.User;

import java.util.List;

public interface UserService extends UserDetailsService {
    User register(String login, String email, String password);
    User find(String login);
    List<Test> getTests(User user);
    List<TestSession> getTestSessions(User user);
}
