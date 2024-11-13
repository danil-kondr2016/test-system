package ru.danilakondr.testsystem.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.stereotype.Repository;
import ru.danilakondr.testsystem.data.Question;

@Repository
public class QuestionDAOImpl implements QuestionDAO {
    private EntityManagerFactory entityManagerFactory;

    public void setEntityManagerFactory(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }

    @Override
    public void add(Question object) {
        EntityManager em = entityManagerFactory.createEntityManager();
        em.persist(object);
    }

    @Override
    public void delete(Question object) {
        EntityManager em = entityManagerFactory.createEntityManager();
        em.remove(object);
    }

    @Override
    public Question get(Long objKey) {
        EntityManager em = entityManagerFactory.createEntityManager();
        return em.find(Question.class, objKey);
    }

    @Override
    public void update(Question object) {
        EntityManager em = entityManagerFactory.createEntityManager();
        em.merge(object);
    }
}
