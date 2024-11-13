package ru.danilakondr.testsystem.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.danilakondr.testsystem.data.Answer;

@Repository
public class AnswerDAOImpl implements AnswerDAO {
    private EntityManagerFactory entityManagerFactory;

    @Autowired
    public void setEntityManagerFactory(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }

    @Override
    public void add(Answer object) {
        EntityManager em = entityManagerFactory.createEntityManager();
        em.persist(object);
    }

    @Override
    public void delete(Answer object) {
        EntityManager em = entityManagerFactory.createEntityManager();
        em.remove(object);
    }

    @Override
    public Answer get(Long objKey) {
        EntityManager em = entityManagerFactory.createEntityManager();
        return em.find(Answer.class, objKey);
    }

    @Override
    public void update(Answer object) {
        EntityManager em = entityManagerFactory.createEntityManager();
        em.merge(object);
    }
}
