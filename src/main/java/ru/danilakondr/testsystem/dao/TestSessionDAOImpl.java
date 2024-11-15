package ru.danilakondr.testsystem.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.danilakondr.testsystem.data.Participant;
import ru.danilakondr.testsystem.data.TestSession;
import ru.danilakondr.testsystem.data.User;
import ru.danilakondr.testsystem.data.UserSession;

import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

@Repository
public class TestSessionDAOImpl implements TestSessionDAO {
    private EntityManagerFactory entityManagerFactory;

    @Autowired
    public void setEntityManagerFactory(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }

    @Override
    public List<Participant> getParticipants(TestSession session) {
        EntityManager em = entityManagerFactory.createEntityManager();
        CriteriaBuilder cb = entityManagerFactory.getCriteriaBuilder();
        CriteriaQuery<Participant> cq = cb.createQuery(Participant.class);
        Root<Participant> root = cq.from(Participant.class);

        Predicate _testSessionIdIs = cb.equal(root.get("testSessionId"), session.getTestSessionId());
        TypedQuery<Participant> query = em.createQuery(cq.where(_testSessionIdIs));
        return query.getResultList();
    }

    @Override
    public Stream<TestSession> getAllActiveTestSessions(UserSession auth) {
        EntityManager em = entityManagerFactory.createEntityManager();

        // Check if user exists and is administrator
        TypedQuery<User> userQuery = em.createQuery("FROM User WHERE userId=:id", User.class);
        userQuery.setParameter("id", auth.getUserId());
        User user = userQuery.getSingleResult();
        if (user.getUserRole() != User.Role.ADMINISTRATOR)
            throw new RuntimeException("Permission denied");

        TypedQuery<TestSession> testSessionQuery = em.createQuery(
                "FROM TestSession WHERE testSessionState=:state",
                TestSession.class).setParameter("state", TestSession.State.ACTIVE);
        return testSessionQuery.getResultStream();
    }

    @Override
    public void add(TestSession object) {
        EntityManager em = entityManagerFactory.createEntityManager();
        em.persist(object);
    }

    @Override
    public void delete(TestSession object) {
        EntityManager em = entityManagerFactory.createEntityManager();
        em.remove(object);
    }

    @Override
    public TestSession get(UUID objKey) {
        EntityManager em = entityManagerFactory.createEntityManager();
        return em.find(TestSession.class, objKey);
    }

    @Override
    public void update(TestSession object) {
        EntityManager em = entityManagerFactory.createEntityManager();
        em.refresh(object);
    }
}
