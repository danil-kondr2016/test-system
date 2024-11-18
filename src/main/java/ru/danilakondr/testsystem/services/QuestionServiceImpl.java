package ru.danilakondr.testsystem.services;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import ru.danilakondr.testsystem.dao.QuestionDAO;
import ru.danilakondr.testsystem.data.Question;

@Service
public class QuestionServiceImpl implements QuestionService {
    private QuestionDAO questionDAO;

    public void setQuestionDAO(QuestionDAO questionDAO) {
        this.questionDAO = questionDAO;
    }

    @Override
    @Transactional
    public Question create(long testId, Question.Type type, String text) {
        Question question = new Question();
        question.setType(type);
        question.setText(text);
        question.setTestId(testId);

        questionDAO.add(question);
        return question;
    }

    @Override
    @Transactional
    public Question get(long questionId) {
        return questionDAO.get(questionId);
    }

    @Override
    @Transactional
    public void update(Question question) {
        questionDAO.update(question);
    }

    @Override
    @Transactional
    public void remove(long questionId) {
        Question question = questionDAO.get(questionId);
        questionDAO.delete(question);
    }
}
