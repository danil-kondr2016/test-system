package ru.danilakondr.testsystem.dao;

import ru.danilakondr.testsystem.data.Answer;
import ru.danilakondr.testsystem.data.Participant;

import java.util.List;

public interface ParticipantDAO extends DataAccessObject<Participant, Long> {
    List<Answer> getAnswers(Participant participant);
}
