package ru.danilakondr.testsystem.services;

import ru.danilakondr.testsystem.data.Test;
import ru.danilakondr.testsystem.data.UserSession;

public interface TestService {
    Test create(UserSession session, String name);
    Test get(long testId);
    void update(Test test);
    void remove(long testId);
}
