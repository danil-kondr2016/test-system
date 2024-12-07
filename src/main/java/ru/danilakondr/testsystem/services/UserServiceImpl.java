package ru.danilakondr.testsystem.services;

import com.github.f4b6a3.uuid.UuidCreator;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.danilakondr.testsystem.dao.*;
import ru.danilakondr.testsystem.data.*;
import ru.danilakondr.testsystem.exception.InvalidCredentialsException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class UserServiceImpl implements UserService, UserDetailsService {
    @Autowired
    private UserDAO userDAO;

    @Autowired
    private UserSessionDAO userSessionDAO;

    @Autowired
    private PasswordResetTokenDAO passwordResetTokenDAO;

    @Autowired
    private TestDAO testDAO;

    @Autowired
    private TestSessionDAO testSessionDAO;

    @Autowired
    @Lazy
    private PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void register(String login, String email, String password) {
        User user = new User();
        user.setLogin(login);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setUserRole(User.Role.ORGANIZATOR);

        userDAO.save(user);
    }

    @Override
    public Optional<User> find(String login) {
        return userDAO.getByLogin(login);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userDAO.getByEmail(email);
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
            userSessionDAO.save(session);

            return session;
        }
        else {
            throw new InvalidCredentialsException("INVALID_CREDENTIALS");
        }
    }

    @Override
    public Optional<UserSession> authenticate(UserSession session) {
        // Remove outdated session
        if (LocalDateTime.now().isAfter(session.getExpires())) {
            logout(session);
            return Optional.empty();
        }

        return Optional.of(session);
    }

    @Override
    public Optional<UserSession> authenticate(UUID sessionId) {
        UserSession session;
        try {
            session = userSessionDAO.getReferenceById(sessionId);
        }
        catch (EntityNotFoundException e) {
            throw new InvalidCredentialsException("INVALID_CREDENTIALS");
        }
        return authenticate(session);
    }

    private void logout(UserSession session) {
        userSessionDAO.delete(session);
    }

    @Override
    public void logout(UUID sessionId) {
        UserSession session;
        try {
            session = userSessionDAO.getReferenceById(sessionId);
        }
        catch (EntityNotFoundException e) {
            throw new InvalidCredentialsException("INVALID_CREDENTIALS");
        }
        logout(session);
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
        passwordResetTokenDAO.save(token);
        return token;
    }

    @Override
    @Transactional
    public void resetPassword(String tokenId, String password) {
        PasswordResetToken token;
        try {
            token = passwordResetTokenDAO.getReferenceById(UuidCreator.fromString(tokenId));
        }
        catch (EntityNotFoundException e) {
            throw new InvalidCredentialsException("INVALID_CREDENTIALS");
        }

        if (LocalDateTime.now().isAfter(token.getExpires())) {
            passwordResetTokenDAO.delete(token);
            throw new InvalidCredentialsException("INVALID_CREDENTIALS");
        }

        token.getUser().setPassword(passwordEncoder.encode(password));
        passwordResetTokenDAO.delete(token);
    }

    @Override
    @Transactional
    public List<Test> getTests(User user) {
        return testDAO.getByUser(user);
    }

    @Override
    @Transactional
    public List<TestSession> getTestSessions(User user) {
        return testSessionDAO.getByUser(user);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return find(username).orElseThrow(() -> new UsernameNotFoundException(username));
    }
}
