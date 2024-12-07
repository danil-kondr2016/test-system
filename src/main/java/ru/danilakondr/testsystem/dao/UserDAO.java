package ru.danilakondr.testsystem.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.danilakondr.testsystem.data.Test;
import ru.danilakondr.testsystem.data.TestSession;
import ru.danilakondr.testsystem.data.User;

import java.util.List;
import java.util.Optional;

public interface UserDAO extends JpaRepository<User, Long> {
    Optional<User> getByLogin(String login);
    Optional<User> getByEmail(String email);
}
