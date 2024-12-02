package ru.danilakondr.testsystem.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.danilakondr.testsystem.data.Answer;

@Repository
public class AnswerDAOImpl implements AnswerDAO {
    @Autowired
    private EntityManager em;

    @Override
    public void add(Answer object) {
        em.persist(object);
    }

    @Override
    public void delete(Answer object) {
        em.remove(object);
    }

    @Override
    public Answer get(Long objKey) {
        return em.find(Answer.class, objKey);
    }

    @Override
    public void update(Answer object) {
        em.merge(object);
    }
}
