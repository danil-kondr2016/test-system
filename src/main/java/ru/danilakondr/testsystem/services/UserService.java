package ru.danilakondr.testsystem.services;

import ru.danilakondr.testsystem.data.Test;
import ru.danilakondr.testsystem.data.TestSession;
import ru.danilakondr.testsystem.data.User;
import ru.danilakondr.testsystem.data.UserSession;

import java.util.List;

public interface UserService {
    User register(String login, String email, String password);
    User find(String login);
    UserSession authenticate(User user, String password);
    void quitSesssion(UserSession session);
    List<Test> getTests(UserSession session);
    List<TestSession> getTestSessions(UserSession session);
}
