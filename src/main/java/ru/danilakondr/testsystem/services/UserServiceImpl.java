package ru.danilakondr.testsystem.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.danilakondr.testsystem.dao.UserDAO;
import ru.danilakondr.testsystem.dao.UserSessionDAO;
import ru.danilakondr.testsystem.data.Test;
import ru.danilakondr.testsystem.data.TestSession;
import ru.danilakondr.testsystem.data.User;
import ru.danilakondr.testsystem.data.UserSession;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    private UserSessionDAO userSessionDAO;
    private UserDAO userDAO;

    @Autowired
    public void setUserDAO(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @Autowired
    public void setUserSessionDAO(UserSessionDAO userSessionDAO) {
        this.userSessionDAO = userSessionDAO;
    }

    @Override
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
    public User find(String login) {
        return userDAO.getByLogin(login);
    }

    @Override
    public UserSession authenticate(User user, String password) {
        if (user.getPassword().compareTo(password) == 0) {
            UserSession session = new UserSession();
            session.setUserId(user.getUserId());
            session.setLoginDate(LocalDateTime.now());
            userSessionDAO.add(session);
            return session;
        }

        return null;
    }

    @Override
    public List<Test> getTests(UserSession session) {
        User user = userDAO.get(session.getUserId());
        if (user == null)
            return null;

        return userDAO.getTests(user);
    }

    @Override
    public List<TestSession> getTestSessions(UserSession session) {
        User user = userDAO.get(session.getUserId());
        if (user == null)
            return null;

        return userDAO.getTestSessions(user);
    }
}
