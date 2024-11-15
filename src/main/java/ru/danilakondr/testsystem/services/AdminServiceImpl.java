package ru.danilakondr.testsystem.services;

import org.springframework.beans.factory.annotation.Autowired;
import ru.danilakondr.testsystem.dao.ParticipantDAO;
import ru.danilakondr.testsystem.dao.TestSessionDAO;
import ru.danilakondr.testsystem.dao.UserSessionDAO;
import ru.danilakondr.testsystem.data.UserSession;
import ru.danilakondr.testsystem.info.SystemInfo;

public class AdminServiceImpl extends UserServiceImpl implements AdminService {
    private UserSessionDAO userSessionDAO;
    private TestSessionDAO testSessionDAO;
    private ParticipantDAO participantDAO;

    @Override
    @Autowired
    public void setUserSessionDAO(UserSessionDAO userSessionDAO) {
        this.userSessionDAO = userSessionDAO;
    }

    @Autowired
    public void setTestSessionDAO(TestSessionDAO testSessionDAO) {
        this.testSessionDAO = testSessionDAO;
    }

    @Autowired
    public void setParticipantDAO(ParticipantDAO participantDAO) {
        this.participantDAO = participantDAO;
    }

    @Override
    public SystemInfo getSystemInfo(UserSession auth) {
        return new SystemInfo(
                userSessionDAO.getAllUserSessions(auth).count(),
                testSessionDAO.getAllActiveTestSessions(auth).count(),
                participantDAO.getAllConnectedParticipants(auth).count());
    }
}
