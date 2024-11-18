package ru.danilakondr.testsystem.services;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.danilakondr.testsystem.dao.ParticipantDAO;
import ru.danilakondr.testsystem.dao.QuestionDAO;
import ru.danilakondr.testsystem.dao.TestDAO;
import ru.danilakondr.testsystem.dao.TestSessionDAO;
import ru.danilakondr.testsystem.data.*;
import ru.danilakondr.testsystem.info.Report;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class TestSessionServiceImpl implements TestSessionService {
    private TestDAO testDAO;
    private TestSessionDAO testSessionDAO;
    private ParticipantDAO participantDAO;
    private QuestionDAO questionDAO;

    @Autowired
    public void setTestDAO(TestDAO testDAO) {
        this.testDAO = testDAO;
    }

    @Autowired
    public void setTestSessionDAO(TestSessionDAO testSessionDAO) {
        this.testSessionDAO = testSessionDAO;
    }

    @Autowired
    public void setParticipantDAO(ParticipantDAO participantDAO) {
        this.participantDAO = participantDAO;
    }

    @Autowired
    public void setQuestionDAO(QuestionDAO questionDAO) {
        this.questionDAO = questionDAO;
    }

    @Override
    @Transactional
    public TestSession create(UserSession auth, long testId) {
        Test test = testDAO.get(testId);
        if (test == null)
            return null;

        if (test.getOrganizatorId() != auth.getUserId())
            throw new RuntimeException("Permission denied");

        TestSession session = new TestSession();
        session.setTestId(testId);
        session.setTestSessionState(TestSession.State.ACTIVE);
        session.setBegin(LocalDateTime.now());
        testSessionDAO.add(session);
        return session;
    }

    @Override
    @Transactional
    public TestSession get(UUID testSessionId) {
        return testSessionDAO.get(testSessionId);
    }

    @Override
    @Transactional
    public void update(TestSession testSession) {
        testSessionDAO.update(testSession);
    }

    @Override
    @Transactional
    public void remove(UUID testSessionId) {
        TestSession session = testSessionDAO.get(testSessionId);
        testSessionDAO.delete(session);
    }

    @Override
    @Transactional
    public Report loadReport(long participantId) {
        Participant participant = participantDAO.get(participantId);
        if (participant == null)
            return null;

        TestSession session = testSessionDAO.get(participant.getTestSessionId());
        List<Answer> answers = participantDAO.getAnswers(participant);
        Report report = new Report(participant.getParticipantId(), participant.getName());
        for (Answer answer: answers) {
            Question question = questionDAO.get(answer.getQuestionId());
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
            if (answer.getVariantId() == variant.getVariantId() && variant.isCorrect()) {
                correct = true;
                break;
            }

            // Check for text equality
            if (variant.getText().equals(answer.getText())) {
                correct = true;
                break;
            }
        }
        return new Report.Answer(session.getTestId(),
                answer.getQuestionId(),
                "",
                answer.getVariantId(),
                answer.getText(),
                correct);
    }
}
