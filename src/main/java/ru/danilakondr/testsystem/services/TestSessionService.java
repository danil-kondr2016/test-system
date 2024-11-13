package ru.danilakondr.testsystem.services;

import ru.danilakondr.testsystem.data.Test;
import ru.danilakondr.testsystem.data.TestSession;
import ru.danilakondr.testsystem.info.Report;

import java.util.UUID;

public interface TestSessionService {
    TestSession create(Test test);
    TestSession get(UUID testSessionId);
    void update(TestSession testSession);
    void remove(UUID testSessionId);
    Report loadReport(TestSession testSession, long participantId);
}
