package ru.danilakondr.testsystem.services;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.danilakondr.testsystem.dao.*;
import ru.danilakondr.testsystem.data.*;
import ru.danilakondr.testsystem.info.Report;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class TestSessionServiceImpl implements TestSessionService {
    @Autowired
    private TestSessionDAO testSessionDAO;

    @Autowired
    private AnswerDAO answerDAO;

    @Override
    @Transactional
    public TestSession create(User user, Test test) {
        TestSession session = new TestSession();
        session.setTest(test);
        session.setTestSessionState(TestSession.State.ACTIVE);
        session.setBegin(LocalDateTime.now());
        testSessionDAO.save(session);
        return session;
    }

    @Override
    @Transactional
    public TestSession get(UUID testSessionId) {
        return testSessionDAO.getReferenceById(testSessionId);
    }

    @Override
    @Transactional
    public void update(TestSession testSession) {
        testSessionDAO.save(testSession);
    }

    @Override
    @Transactional
    public void remove(TestSession session) {
        testSessionDAO.delete(session);
    }

    @Override
    @Transactional
    public Report loadReport(Participant participant) {
        TestSession session = participant.getTestSession();
        List<Answer> answers = answerDAO.findByParticipant(participant);
        Report report = new Report(participant.getId(), participant.getName());
        for (Answer answer: answers) {
            Question question = answer.getQuestion();
            Report.Answer reportAnswer = extractAnswer(answer, question, session);
            report.putAnswer(reportAnswer);
        }

        return report;
    }

    @Transactional
    private Report.Answer extractAnswer(Answer answer, Question question, TestSession session) {
        List<AnswerVariant> variants = question.getVariants();

        boolean correct = false;
        for (AnswerVariant variant: variants) {
            // Check for selection types
            if (answer.getVariant() == variant && variant.isCorrect()) {
                correct = true;
                break;
            }

            // Check for text equality
            if (variant.getText().equals(answer.getText())) {
                correct = true;
                break;
            }
        }
        return new Report.Answer(session.getTest().getId(),
                answer.getQuestion().getId(),
                answer.getQuestion().getText(),
                answer.getVariant().getId(),
                answer.getText(),
                correct);
    }
}
