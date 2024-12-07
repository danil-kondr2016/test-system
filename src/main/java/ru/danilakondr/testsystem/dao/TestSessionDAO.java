package ru.danilakondr.testsystem.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.danilakondr.testsystem.data.TestSession;
import ru.danilakondr.testsystem.data.Participant;
import ru.danilakondr.testsystem.data.User;

import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

public interface TestSessionDAO extends JpaRepository<TestSession, UUID> {
    @Query("SELECT ts FROM TestSession ts INNER JOIN ts.test t WHERE t.user=?1")
    List<TestSession> getByUser(User user);
    @Query("FROM TestSession WHERE testSessionState=ACTIVE")
    Stream<TestSession> getAllActiveTestSessions();
}
