package ru.danilakondr.testsystem.services;

import ru.danilakondr.testsystem.data.TestSession;
import ru.danilakondr.testsystem.info.Report;
import ru.danilakondr.testsystem.data.User;

import java.util.UUID;

public interface TestSessionService {
    TestSession create(User user, long testId);
    TestSession get(UUID testSessionId);
    void update(TestSession testSession);
    void remove(UUID testSessionId);
    Report loadReport(long participantId);
}
