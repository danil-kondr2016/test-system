package ru.danilakondr.testsystem.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.danilakondr.testsystem.data.Test;
import ru.danilakondr.testsystem.data.TestSession;
import ru.danilakondr.testsystem.data.User;

import java.util.List;

@Repository
@Transactional
public class UserDAOImpl implements UserDAO {
    @Autowired
    private EntityManager em;

    @Override
    public User getByLogin(String login) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<User> cq = cb.createQuery(User.class);
        Root<User> root = cq.from(User.class);

        Predicate _userIdIs = cb.equal(root.get("login"), login);
        TypedQuery<User> query = em.createQuery(cq.where(_userIdIs));
        try {
            return query.getSingleResult();
        }
        catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public List<Test> getTests(User user) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Test> cq = cb.createQuery(Test.class);
        Root<Test> root = cq.from(Test.class);

        Predicate _organizerIdIs = cb.equal(root.get("organizatorId"), user.getUserId());
        TypedQuery<Test> query = em.createQuery(cq.where(_organizerIdIs));
        return query.getResultList();
    }

    @Override
    public List<TestSession> getTestSessions(User user) {
        TypedQuery<TestSession> query = em.createQuery(
                "SELECT s FROM Test t INNER JOIN TestSession t.testId s WHERE t.userId=:id",
                TestSession.class
        );
        query.setParameter("id", user.getUserId());

        return query.getResultList();
    }

    @Override
    @Transactional
    public void add(User object) {
        em.persist(object);
    }

    @Override
    @Transactional
    public void delete(User object) {
        em.remove(object);
    }

    @Override
    @Transactional
    public User get(Long objKey) {
        return em.find(User.class, objKey);
    }

    @Override
    @Transactional
    public void update(User object) {
        em.merge(object);
    }
}
