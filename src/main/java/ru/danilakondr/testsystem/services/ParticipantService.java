package ru.danilakondr.testsystem.services;

import ru.danilakondr.testsystem.data.Answer;
import ru.danilakondr.testsystem.data.Participant;

import java.util.UUID;

public interface ParticipantService {
    Participant create(UUID testSessionID, String name);
    Participant get(long participantId);
    void update(Participant participant);
    void remove(long participantId);
    void putAnswer(long participantId, Answer answer);
}
