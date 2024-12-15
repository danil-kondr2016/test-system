package ru.danilakondr.testsystem.services;

import ru.danilakondr.testsystem.data.Answer;
import ru.danilakondr.testsystem.data.Participant;
import ru.danilakondr.testsystem.data.TestSession;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ParticipantService {
    Participant create(TestSession testSession, String name);
    Optional<Participant> get(UUID participantId);
    void remove(Participant participant);
    void putAnswer(Participant participant, Answer answer);
    void complete(UUID participantId);
    boolean validate(Participant participant);
    List<Participant> getByTestSession(TestSession session);
}
