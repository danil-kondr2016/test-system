package ru.danilakondr.testsystem.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.stereotype.Repository;
import ru.danilakondr.testsystem.data.*;

import java.util.List;
import java.util.stream.Stream;

@Repository
public class ParticipantDAOImpl implements ParticipantDAO {
    private EntityManagerFactory entityManagerFactory;

    public void setEntityManagerFactory(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }

    @Override
    public List<Answer> getAnswers(Participant participant) {
        EntityManager em = entityManagerFactory.createEntityManager();
        CriteriaBuilder cb = entityManagerFactory.getCriteriaBuilder();
        CriteriaQuery<Answer> cq = cb.createQuery(Answer.class);
        Root<Answer> root = cq.from(Answer.class);

        Predicate _testIdIs = cb.equal(root.get("participantId"), participant.getParticipantId());
        TypedQuery<Answer> query = em.createQuery(cq.where(_testIdIs));
        return query.getResultList();
    }

    @Override
    public Stream<Participant> getAllConnectedParticipants(UserSession auth) {
        EntityManager em = entityManagerFactory.createEntityManager();

        // Check if user exists and is administrator
        TypedQuery<User> userQuery = em.createQuery("FROM User WHERE userId=:id", User.class);
        userQuery.setParameter("id", auth.getUserId());
        User user = userQuery.getSingleResult();
        if (user.getUserRole() != User.Role.ADMINISTRATOR)
            throw new RuntimeException("Permission denied");

        TypedQuery<Participant> participantQuery = em.createQuery(
                "SELECT p FROM TestSession t INNER JOIN t.testSessionId p " +
                        "WHERE t.testSessionState=:state",
                Participant.class
        ).setParameter("state", TestSession.State.ACTIVE);
        return participantQuery.getResultStream();
    }

    @Override
    public void add(Participant object) {
        EntityManager em = entityManagerFactory.createEntityManager();
        em.persist(object);
    }

    @Override
    public void delete(Participant object) {
        EntityManager em = entityManagerFactory.createEntityManager();
        em.remove(object);
    }

    @Override
    public Participant get(Long objKey) {
        EntityManager em = entityManagerFactory.createEntityManager();
        return em.find(Participant.class, objKey);
    }

    @Override
    public void update(Participant object) {
        EntityManager em = entityManagerFactory.createEntityManager();
        em.merge(object);
    }
}
