package ru.danilakondr.testsystem.services;

import ru.danilakondr.testsystem.data.Answer;
import ru.danilakondr.testsystem.data.Participant;
import ru.danilakondr.testsystem.data.TestSession;

public interface ParticipantService {
    Participant create(TestSession testSession, String name);
    Participant get(long participantId);
    void remove(Participant participant);
    void putAnswer(Participant participant, Answer answer);
}
