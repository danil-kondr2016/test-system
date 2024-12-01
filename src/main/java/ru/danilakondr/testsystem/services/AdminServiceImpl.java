package ru.danilakondr.testsystem.services;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.danilakondr.testsystem.dao.ParticipantDAO;
import ru.danilakondr.testsystem.dao.TestSessionDAO;
import ru.danilakondr.testsystem.info.SystemInfo;

@Service
public class AdminServiceImpl implements AdminService {
    private TestSessionDAO testSessionDAO;
    private ParticipantDAO participantDAO;

    @Autowired
    public void setTestSessionDAO(TestSessionDAO testSessionDAO) {
        this.testSessionDAO = testSessionDAO;
    }

    @Autowired
    public void setParticipantDAO(ParticipantDAO participantDAO) {
        this.participantDAO = participantDAO;
    }

    @Override
    @Transactional
    public SystemInfo getSystemInfo() {
        return new SystemInfo(
                testSessionDAO.getAllActiveTestSessions().count(),
                participantDAO.getAllConnectedParticipants().count());
    }
}
