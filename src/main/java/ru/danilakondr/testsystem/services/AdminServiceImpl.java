package ru.danilakondr.testsystem.services;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.danilakondr.testsystem.dao.ParticipantDAO;
import ru.danilakondr.testsystem.dao.TestSessionDAO;
import ru.danilakondr.testsystem.info.SystemInfo;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {
    private final TestSessionDAO testSessionDAO;
    private final ParticipantDAO participantDAO;

    @Override
    @Transactional
    public SystemInfo getSystemInfo() {
        return new SystemInfo(
                testSessionDAO.getAllActiveTestSessions().count(),
                participantDAO.findAllConnectedParticipants().count());
    }
}
