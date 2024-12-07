package ru.danilakondr.testsystem.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.danilakondr.testsystem.data.Question;
import ru.danilakondr.testsystem.data.Test;
import ru.danilakondr.testsystem.data.User;

import java.util.List;

public interface TestDAO extends JpaRepository<Test, Long> {
    List<Test> getByUser(User user);
}
