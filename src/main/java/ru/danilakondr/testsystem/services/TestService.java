package ru.danilakondr.testsystem.services;

import ru.danilakondr.testsystem.data.Test;
import ru.danilakondr.testsystem.data.User;

import java.util.List;

public interface TestService {
    Test create(User user, String name);
    Test get(long testId);
    void remove(Test test);
    List<Test> getByUser(User user);
}
