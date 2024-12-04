package ru.danilakondr.testsystem.dao;

import ru.danilakondr.testsystem.data.Test;
import ru.danilakondr.testsystem.data.TestSession;
import ru.danilakondr.testsystem.data.User;

import java.util.List;

public interface UserDAO extends DataAccessObject<User, Long> {
    User getByLogin(String login);
    User getByEmail(String email);
    List<Test> getTests(User user);
    List<TestSession> getTestSessions(User user);
}
