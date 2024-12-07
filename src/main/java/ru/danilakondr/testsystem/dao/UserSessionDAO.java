package ru.danilakondr.testsystem.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.danilakondr.testsystem.data.UserSession;

import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

public interface UserSessionDAO extends JpaRepository<UserSession, UUID> {
    @Query("FROM UserSession")
    Stream<UserSession> getAllUserSessions();
}
