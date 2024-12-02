package ru.danilakondr.testsystem.services;

import ru.danilakondr.testsystem.data.Test;
import ru.danilakondr.testsystem.data.User;

public interface TestService {
    Test create(User user, String name);
    Test get(long testId);
    void update(Test test);
    void remove(Test test);
}
