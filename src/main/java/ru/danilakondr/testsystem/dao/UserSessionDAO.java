package ru.danilakondr.testsystem.dao;

import ru.danilakondr.testsystem.data.UserSession;

import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

public interface UserSessionDAO extends DataAccessObject<UserSession, UUID> {
    Stream<UserSession> getAllUserSessions(UserSession auth);
}
