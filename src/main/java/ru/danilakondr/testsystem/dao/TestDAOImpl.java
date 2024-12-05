package ru.danilakondr.testsystem.dao;

import jakarta.persistence.*;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.danilakondr.testsystem.data.Question;
import ru.danilakondr.testsystem.data.Test;

import java.util.List;

@Repository
public class TestDAOImpl implements TestDAO {
    @Autowired
    private EntityManager em;

    @Override
    public List<Question> getQuestions(Test test) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Question> cq = cb.createQuery(Question.class);
        Root<Question> root = cq.from(Question.class);

        Predicate _testIdIs = cb.equal(root.get("id"), test.getId());
        TypedQuery<Question> query = em.createQuery(cq.where(_testIdIs));
        return query.getResultList();
    }

    @Override
    public void add(Test object) {
        em.persist(object);
    }

    @Override
    public void delete(Test object) {
        em.remove(object);
    }

    @Override
    public Test get(Long objKey) {
        return em.find(Test.class, objKey);
    }

    @Override
    public void update(Test object) {
        em.merge(object);
    }
}
