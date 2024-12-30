package ru.danilakondr.testsystem.services;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.danilakondr.testsystem.dao.ParticipantDAO;
import ru.danilakondr.testsystem.dao.TestSessionDAO;
import ru.danilakondr.testsystem.dao.UserDAO;
import ru.danilakondr.testsystem.data.User;
import ru.danilakondr.testsystem.info.SystemInfo;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {
    private final TestSessionDAO testSessionDAO;
    private final ParticipantDAO participantDAO;
    private final UserDAO userDAO;
    @Autowired
    @Lazy
    private PasswordEncoder passwordEncoder;

    @Override
    public void register(String email, String password) {
        Optional<User> user = userDAO.getByLogin("admin");
        if (user.isPresent()) {
            User ref = userDAO.getReferenceById(user.get().getId());
            ref.setUserRole(User.Role.ADMINISTRATOR);
            ref.setEmail(email);
            ref.setPassword(passwordEncoder.encode(password));
        }
        else {
            User admin = new User();
            admin.setLogin("admin");
            admin.setEmail(email);
            admin.setPassword(passwordEncoder.encode(password));
            admin.setUserRole(User.Role.ADMINISTRATOR);
            userDAO.saveAndFlush(admin);
        }
    }

    @Override
    @Transactional
    public SystemInfo getSystemInfo() {
        return new SystemInfo(
                testSessionDAO.getAllActiveTestSessions().count(),
                participantDAO.findAllConnectedParticipants().count());
    }
}
