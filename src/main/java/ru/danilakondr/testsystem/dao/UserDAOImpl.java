package ru.danilakondr.testsystem.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.danilakondr.testsystem.data.Test;
import ru.danilakondr.testsystem.data.TestSession;
import ru.danilakondr.testsystem.data.User;

import java.util.List;

@Repository
public class UserDAOImpl implements UserDAO {
    private EntityManagerFactory entityManagerFactory;

    @Autowired
    public void setEntityManagerFactory(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }

    @Override
    public User getByLogin(String login) {
        EntityManager em = entityManagerFactory.createEntityManager();
        CriteriaBuilder cb = entityManagerFactory.getCriteriaBuilder();
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
        EntityManager em = entityManagerFactory.createEntityManager();
        CriteriaBuilder cb = entityManagerFactory.getCriteriaBuilder();
        CriteriaQuery<Test> cq = cb.createQuery(Test.class);
        Root<Test> root = cq.from(Test.class);

        Predicate _organizerIdIs = cb.equal(root.get("organizatorId"), user.getUserId());
        TypedQuery<Test> query = em.createQuery(cq.where(_organizerIdIs));
        return query.getResultList();
    }

    @Override
    public List<TestSession> getTestSessions(User user) {
        EntityManager em = entityManagerFactory.createEntityManager();
        TypedQuery<TestSession> query = em.createQuery(
                "SELECT s FROM Test t INNER JOIN TestSession t.testId s WHERE t.userId=:id",
                TestSession.class
        );
        query.setParameter("id", user.getUserId());

        return query.getResultList();
    }

    @Override
    public void add(User object) {
        EntityManager em = entityManagerFactory.createEntityManager();
        em.persist(object);
    }

    @Override
    public void delete(User object) {
        EntityManager em = entityManagerFactory.createEntityManager();
        em.remove(object);
    }

    @Override
    public User get(Long objKey) {
        EntityManager em = entityManagerFactory.createEntityManager();
        return em.find(User.class, objKey);
    }

    @Override
    public void update(User object) {
        EntityManager em = entityManagerFactory.createEntityManager();
        em.merge(object);
    }
}
