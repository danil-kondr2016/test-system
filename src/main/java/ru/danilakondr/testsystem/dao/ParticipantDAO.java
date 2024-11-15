package ru.danilakondr.testsystem.dao;

import ru.danilakondr.testsystem.data.Answer;
import ru.danilakondr.testsystem.data.Participant;
import ru.danilakondr.testsystem.data.UserSession;

import java.util.List;
import java.util.stream.Stream;

public interface ParticipantDAO extends DataAccessObject<Participant, Long> {
    List<Answer> getAnswers(Participant participant);
    Stream<Participant> getAllConnectedParticipants(UserSession auth);
}
