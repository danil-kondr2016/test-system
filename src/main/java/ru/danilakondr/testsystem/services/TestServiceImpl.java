package ru.danilakondr.testsystem.services;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.danilakondr.testsystem.dao.TestDAO;
import ru.danilakondr.testsystem.data.Test;
import ru.danilakondr.testsystem.data.User;

@Service
public class TestServiceImpl implements TestService {
    @Autowired
    private TestDAO testDAO;

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
    public void update(Test test) {
        testDAO.save(test);
    }

    @Override
    @Transactional
    public void remove(Test test) {
        testDAO.delete(test);
    }
}
