package ru.danilakondr.testsystem.services;

import org.springframework.security.core.userdetails.UserDetailsService;
import ru.danilakondr.testsystem.data.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserService extends UserDetailsService {
    void register(String login, String email, String password);
    Optional<User> find(String login);
    Optional<User> findByEmail(String email);
    boolean validate(User user, String password);
    void changePassword(User user, String password);

    UserSession login(String login, String password);
    Optional<UserSession> authenticate(UserSession session);
    Optional<UserSession> authenticate(UUID sessionId);
    void logout(UUID sessionId);

    PasswordResetToken requestPasswordReset(String identifier);
    void resetPassword(String tokenId, String password);
}
