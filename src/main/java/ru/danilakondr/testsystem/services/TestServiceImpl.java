package ru.danilakondr.testsystem.services;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.danilakondr.testsystem.dao.TestDAO;
import ru.danilakondr.testsystem.data.Test;
import ru.danilakondr.testsystem.data.UserSession;

@Service
public class TestServiceImpl implements TestService {
    private TestDAO testDAO;

    @Autowired
    public void setTestDAO(TestDAO testDAO) {
        this.testDAO = testDAO;
    }

    @Override
    @Transactional
    public Test create(UserSession session, String name) {
        Test newTest = new Test();
        newTest.setOrganizatorId(session.getUserId());
        newTest.setName(name);

        testDAO.add(newTest);
        return newTest;
    }

    @Override
    @Transactional
    public Test get(long testId) {
        return testDAO.get(testId);
    }

    @Override
    @Transactional
    public void update(Test test) {
        testDAO.update(test);
    }

    @Override
    @Transactional
    public void remove(long testId) {
        Test test = testDAO.get(testId);
        testDAO.delete(test);
    }
}
