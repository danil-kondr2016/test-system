package ru.danilakondr.testsystem.dao;

import ru.danilakondr.testsystem.data.TestSession;
import ru.danilakondr.testsystem.data.Participant;

import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

public interface TestSessionDAO extends DataAccessObject<TestSession, UUID> {
    List<Participant> getParticipants(TestSession session);
    Stream<TestSession> getAllActiveTestSessions();
}
