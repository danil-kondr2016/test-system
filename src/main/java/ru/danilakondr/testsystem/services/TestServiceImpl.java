package ru.danilakondr.testsystem.services;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.danilakondr.testsystem.dao.QuestionDAO;
import ru.danilakondr.testsystem.dao.TestDAO;
import ru.danilakondr.testsystem.data.Question;
import ru.danilakondr.testsystem.data.Test;
import ru.danilakondr.testsystem.data.User;

import java.util.List;

@Service
public class TestServiceImpl implements TestService {
    @Autowired
    private TestDAO testDAO;

    @Autowired
    private QuestionDAO questionDAO;

    @Override
    @Transactional
    public Test create(User user, String name) {
        Test newTest = new Test();
        newTest.setUser(user);
        newTest.setName(name);

        testDAO.save(newTest);
        return newTest;
    }

    @Override
    @Transactional
    public Test get(long testId) {
        return testDAO.getReferenceById(testId);
    }

    @Override
    @Transactional
    public void remove(Test test) {
        testDAO.delete(test);
    }

    @Override
    @Transactional
    public List<Test> getByUser(User user) {
        return testDAO.getByUser(user);
    }

    @Override
    @Transactional
    public void reorderQuestions(Test test, long[] questionIds) {
        if (questionIds.length != test.getQuestions().size())
            throw new IllegalArgumentException("questionIds.length != test.getQuestions().size()");

        long order = 1;
        for (long questionId : questionIds) {
            Question question = questionDAO.getReferenceById(questionId);
            question.setOrder(order++);
        }
    }
}
