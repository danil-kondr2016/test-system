package ru.danilakondr.testsystem.dao;

import jakarta.persistence.*;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.transaction.Transactional;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.danilakondr.testsystem.data.Question;
import ru.danilakondr.testsystem.data.Test;

import java.util.List;

@Repository
public class TestDAOImpl implements TestDAO {
    private EntityManagerFactory entityManagerFactory;

    @Autowired
    public void setEntityManagerFactory(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }

    @Override
    @Transactional
    public List<Question> getQuestions(Test test) {
        EntityManager em = entityManagerFactory.createEntityManager();
        CriteriaBuilder cb = entityManagerFactory.getCriteriaBuilder();
        CriteriaQuery<Question> cq = cb.createQuery(Question.class);
        Root<Question> root = cq.from(Question.class);

        Predicate _testIdIs = cb.equal(root.get("testId"), test.getTestId());
        TypedQuery<Question> query = em.createQuery(cq.where(_testIdIs));
        return query.getResultList();
    }

    @Override
    @Transactional
    public void add(Test object) {
        EntityManager em = entityManagerFactory.createEntityManager();
        em.persist(object);
    }

    @Override
    @Transactional
    public void delete(Test object) {
        EntityManager em = entityManagerFactory.createEntityManager();
        em.remove(object);
    }

    @Override
    @Transactional
    public Test get(Long objKey) {
        EntityManager em = entityManagerFactory.createEntityManager();
        return em.find(Test.class, objKey);
    }

    @Override
    @Transactional
    public void update(Test object) {
        EntityManager em = entityManagerFactory.createEntityManager();
        em.merge(object);
    }
}
