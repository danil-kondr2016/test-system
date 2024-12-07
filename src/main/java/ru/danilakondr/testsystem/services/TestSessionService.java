package ru.danilakondr.testsystem.services;

import ru.danilakondr.testsystem.data.Participant;
import ru.danilakondr.testsystem.data.Test;
import ru.danilakondr.testsystem.data.TestSession;
import ru.danilakondr.testsystem.info.Report;
import ru.danilakondr.testsystem.data.User;

import java.util.List;
import java.util.UUID;

public interface TestSessionService {
    TestSession create(User user, Test test);
    TestSession get(UUID testSessionId);
    void update(TestSession testSession);
    void remove(TestSession testSession);
    Report loadReport(Participant participant);
    List<TestSession> getByUser(User user);
}
