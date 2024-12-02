package ru.danilakondr.testsystem.services;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.danilakondr.testsystem.dao.AnswerDAO;
import ru.danilakondr.testsystem.dao.ParticipantDAO;
import ru.danilakondr.testsystem.data.Answer;
import ru.danilakondr.testsystem.data.Participant;
import ru.danilakondr.testsystem.data.TestSession;

@Service
public class ParticipantServiceImpl implements ParticipantService {
    private ParticipantDAO participantDAO;
    private AnswerDAO answerDAO;

    @Autowired
    public void setParticipantDAO(ParticipantDAO participantDAO) {
        this.participantDAO = participantDAO;
    }

    @Autowired
    public void setAnswerDAO(AnswerDAO answerDAO) {
        this.answerDAO = answerDAO;
    }

    @Override
    @Transactional
    public Participant create(TestSession testSession, String name) {
        Participant participant = new Participant();
        participant.setTestSession(testSession);
        participant.setName(name);

        participantDAO.add(participant);
        return participant;
    }

    @Override
    @Transactional
    public Participant get(long participantId) {
        return participantDAO.get(participantId);
    }

    @Override
    @Transactional
    public void update(Participant participant) {
        participantDAO.update(participant);
    }

    @Override
    @Transactional
    public void remove(Participant participant) {
        participantDAO.delete(participant);
    }

    @Override
    @Transactional
    public void putAnswer(Participant participant, Answer answer) {
        answer.setParticipant(participant);
        answerDAO.add(answer);
    }
}
