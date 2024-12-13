package ru.danilakondr.testsystem.services;

import com.github.f4b6a3.uuid.UuidCreator;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.danilakondr.testsystem.dao.AnswerDAO;
import ru.danilakondr.testsystem.dao.ParticipantDAO;
import ru.danilakondr.testsystem.data.Answer;
import ru.danilakondr.testsystem.data.Participant;
import ru.danilakondr.testsystem.data.TestSession;

import java.util.Optional;
import java.util.UUID;

@Service
public class ParticipantServiceImpl implements ParticipantService {
    @Autowired
    private ParticipantDAO participantDAO;

    @Autowired
    private AnswerDAO answerDAO;

    @Override
    @Transactional
    public Participant create(TestSession testSession, String name) {
        Participant participant = new Participant();
        participant.setId(UuidCreator.getTimeOrderedEpochPlus1());
        participant.setTestSession(testSession);
        participant.setName(name);

        participantDAO.save(participant);
        return participant;
    }

    @Override
    @Transactional
    public Optional<Participant> get(UUID participantId) {
        return participantDAO.findById(participantId);
    }

    @Override
    @Transactional
    public void remove(Participant participant) {
        participantDAO.delete(participant);
    }

    @Transactional
    public void complete(UUID participantId) {
        Participant participant = participantDAO.getReferenceById(participantId);
        participant.setState(Participant.State.COMPLETED);
    }

    @Override
    @Transactional
    public void putAnswer(Participant participant, Answer answer) {
        answer.setParticipant(participant);
        answerDAO.save(answer);
    }

    @Override
    public boolean validate(Participant participant) {
        if (participant == null)
            return false;

        if (participant.getTestSession().getTestSessionState() != TestSession.State.ACTIVE)
            return false;

        if (participant.getState() == Participant.State.COMPLETED)
            return false;

        return true;
    }
}
