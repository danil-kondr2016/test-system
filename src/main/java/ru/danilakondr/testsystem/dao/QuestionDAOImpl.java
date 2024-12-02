package ru.danilakondr.testsystem.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.danilakondr.testsystem.data.Question;

@Repository
public class QuestionDAOImpl implements QuestionDAO {
    @Autowired
    private EntityManager em;

    @Override
    public void add(Question object) {
        em.persist(object);
    }

    @Override
    public void delete(Question object) {
        em.remove(object);
    }

    @Override
    public Question get(Long objKey) {
        return em.find(Question.class, objKey);
    }

    @Override
    public void update(Question object) {
        em.merge(object);
    }
}
