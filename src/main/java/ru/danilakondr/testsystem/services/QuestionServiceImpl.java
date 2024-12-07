package ru.danilakondr.testsystem.services;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.danilakondr.testsystem.dao.QuestionDAO;
import ru.danilakondr.testsystem.data.AnswerVariant;
import ru.danilakondr.testsystem.data.Question;
import ru.danilakondr.testsystem.data.Test;

import java.util.List;

@Service
public class QuestionServiceImpl implements QuestionService {
    @Autowired
    private QuestionDAO questionDAO;

    @Override
    @Transactional
    public Question create(Test test, Question.Type type, String text, List<AnswerVariant> variants) {
        Question question = new Question();
        question.setType(type);
        question.setText(text);
        question.setTest(test);
        question.setVariants(variants);
        variants.forEach(x -> x.setQuestion(question));

        questionDAO.save(question);
        return question;
    }

    @Override
    @Transactional
    public Question get(long questionId) {
        return questionDAO.getReferenceById(questionId);
    }

    @Override
    @Transactional
    public void update(Question question) {
        questionDAO.save(question);
    }

    @Override
    @Transactional
    public void remove(Question question) {
        questionDAO.delete(question);
    }
}
