package ru.danilakondr.testsystem.dao;

import ru.danilakondr.testsystem.data.Test;
import ru.danilakondr.testsystem.data.User;

import java.util.List;

public interface UserDAO extends DataAccessObject<User, Long> {
    List<Test> getTests(User user);
}
