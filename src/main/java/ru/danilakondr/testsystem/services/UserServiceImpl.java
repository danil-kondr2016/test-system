package ru.danilakondr.testsystem.services;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.danilakondr.testsystem.dao.UserDAO;
import ru.danilakondr.testsystem.data.Test;
import ru.danilakondr.testsystem.data.TestSession;
import ru.danilakondr.testsystem.data.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService, UserDetailsService {
    private UserDAO userDAO;

    @Autowired
    public void setUserDAO(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @Override
    @Transactional
    public User register(String login, String email, String password) {
        if (userDAO.getByLogin(login) != null)
            throw new IllegalArgumentException("User with login " + login + " already exists");

        User user = new User();
        user.setLogin(login);
        user.setEmail(email);
        user.setPassword(password);
        user.setUserRole(User.Role.ORGANIZATOR);

        userDAO.add(user);
        return user;
    }

    @Override
    public Optional<User> find(String login) {
        return Optional.ofNullable(userDAO.getByLogin(login));
    }

    @Override
    @Transactional
    public List<Test> getTests(User user) {
        return userDAO.getTests(user);
    }

    @Override
    @Transactional
    public List<TestSession> getTestSessions(User user) {
        return userDAO.getTestSessions(user);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserDetails user = find(username).orElseThrow(() -> new UsernameNotFoundException(username));
        return user;
    }
}
