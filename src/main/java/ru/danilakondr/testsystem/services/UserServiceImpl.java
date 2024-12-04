package ru.danilakondr.testsystem.services;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.danilakondr.testsystem.dao.PasswordResetTokenDAO;
import ru.danilakondr.testsystem.dao.UserDAO;
import ru.danilakondr.testsystem.dao.UserSessionDAO;
import ru.danilakondr.testsystem.data.*;
import ru.danilakondr.testsystem.exception.InvalidCredentialsException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class UserServiceImpl implements UserService, UserDetailsService {
    private UserDAO userDAO;

    @Autowired
    private UserSessionDAO userSessionDAO;

    @Autowired
    private PasswordResetTokenDAO passwordResetTokenDAO;

    @Autowired
    @Lazy
    private PasswordEncoder passwordEncoder;

    @Autowired
    public void setUserDAO(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @Override
    @Transactional
    public User register(String login, String email, String password) {
        User user = new User();
        user.setLogin(login);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setUserRole(User.Role.ORGANIZATOR);

        userDAO.add(user);
        return user;
    }

    @Override
    public Optional<User> find(String login) {
        return Optional.ofNullable(userDAO.getByLogin(login));
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return Optional.ofNullable(userDAO.getByEmail(email));
    }

    @Override
    public boolean validate(User user, String password) {
        return passwordEncoder.matches(password, user.getPassword());
    }

    @Override
    public UserSession login(String login, String password) {
        Optional<User> user = find(login);
        if (user.isEmpty()) {
            throw new InvalidCredentialsException("INVALID_CREDENTIALS");
        }

        if (validate(user.get(), password)) {
            LocalDateTime loginDate = LocalDateTime.now();

            UserSession session = new UserSession();
            session.setUser(user.get());
            session.setLoginDate(loginDate);
            session.setExpires(loginDate.plusDays(30));
            userSessionDAO.add(session);

            return session;
        }
        else {
            throw new InvalidCredentialsException("INVALID_CREDENTIALS");
        }
    }

    @Override
    public Optional<UserSession> authenticate(UserSession session) {
        if (userSessionDAO.get(session.getSessionId()) == null)
            return Optional.empty();

        // Remove outdated session
        if (LocalDateTime.now().isAfter(session.getExpires())) {
            logout(session);
            return Optional.empty();
        }

        return Optional.of(session);
    }

    @Override
    public Optional<UserSession> authenticate(UUID sessionId) {
        UserSession session = userSessionDAO.get(sessionId);
        if (session == null)
            return Optional.empty();

        return authenticate(session);
    }

    @Override
    public void logout(UserSession session) {
        userSessionDAO.delete(session);
    }

    @Override
    public void changePassword(User user, String password) {
        user.setPassword(passwordEncoder.encode(password));
    }

    @Override
    @Transactional
    public PasswordResetToken requestPasswordReset(String email) {
        Optional<User> user = findByEmail(email);
        if (user.isEmpty()) {
            throw new InvalidCredentialsException(email);
        }

        PasswordResetToken token = new PasswordResetToken(user.get());
        passwordResetTokenDAO.add(token);
        return token;
    }

    @Override
    @Transactional
    public void changePassword(PasswordResetToken token, String password) {
        if (LocalDateTime.now().isAfter(token.getExpires())) {
            passwordResetTokenDAO.delete(token);
            throw new InvalidCredentialsException("INVALID_CREDENTIALS");
        }

        token.getUser().setPassword(passwordEncoder.encode(password));
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
