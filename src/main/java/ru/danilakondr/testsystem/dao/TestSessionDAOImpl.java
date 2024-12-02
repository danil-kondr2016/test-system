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

import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

@Repository
public class TestSessionDAOImpl implements TestSessionDAO {
    @Autowired
    private EntityManager em;

    @Override
    public List<Participant> getParticipants(TestSession session) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Participant> cq = cb.createQuery(Participant.class);
        Root<Participant> root = cq.from(Participant.class);

        Predicate _testSessionIdIs = cb.equal(root.get("testSessionId"), session.getTestSessionId());
        TypedQuery<Participant> query = em.createQuery(cq.where(_testSessionIdIs));
        return query.getResultList();
    }

    @Override
    public Stream<TestSession> getAllActiveTestSessions() {
        TypedQuery<TestSession> testSessionQuery = em.createQuery(
                "FROM TestSession WHERE testSessionState=:state",
                TestSession.class).setParameter("state", TestSession.State.ACTIVE);
        return testSessionQuery.getResultStream();
    }

    @Override
    public void add(TestSession object) {
        em.persist(object);
    }

    @Override
    public void delete(TestSession object) {
        em.remove(object);
    }

    @Override
    public TestSession get(UUID objKey) {
        return em.find(TestSession.class, objKey);
    }

    @Override
    public void update(TestSession object) {
        em.refresh(object);
    }
}
