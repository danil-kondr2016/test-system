package ru.danilakondr.testsystem.dao;

import jakarta.transaction.Transactional;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.danilakondr.testsystem.data.Answer;

@Repository
public class AnswerDAOImpl implements AnswerDAO {
    private SessionFactory sessionFactory;

    @Autowired
    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    @Transactional
    public void add(Answer object) {
        Session session = sessionFactory.getCurrentSession();
        session.persist(object);
    }

    @Override
    public void delete(Answer object) {
        Session session = sessionFactory.getCurrentSession();
        session.remove(object);
    }

    @Override
    public Answer get(Long objKey) {
        Session session = sessionFactory.getCurrentSession();
        return session.get(Answer.class, objKey);
    }

    @Override
    public void update(Answer object) {
        Session session = sessionFactory.getCurrentSession();
        session.merge(object);
    }
}
