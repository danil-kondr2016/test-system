package ru.danilakondr.testsystem.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
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
    @Transactional
    public void add(Answer object) {
        EntityManager em = entityManagerFactory.createEntityManager();
        em.persist(object);
    }

    @Override
    @Transactional
    public void delete(Answer object) {
        EntityManager em = entityManagerFactory.createEntityManager();
        em.remove(object);
    }

    @Override
    @Transactional
    public Answer get(Long objKey) {
        EntityManager em = entityManagerFactory.createEntityManager();
        return em.find(Answer.class, objKey);
    }

    @Override
    @Transactional
    public void update(Answer object) {
        EntityManager em = entityManagerFactory.createEntityManager();
        em.merge(object);
    }
}
