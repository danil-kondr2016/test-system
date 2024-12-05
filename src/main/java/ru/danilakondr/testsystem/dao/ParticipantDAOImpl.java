package ru.danilakondr.testsystem.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.danilakondr.testsystem.data.*;

import java.util.List;
import java.util.stream.Stream;

@Repository
public class ParticipantDAOImpl implements ParticipantDAO {
    @Autowired
    private EntityManager em;

    @Override
    public List<Answer> getAnswers(Participant participant) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Answer> cq = cb.createQuery(Answer.class);
        Root<Answer> root = cq.from(Answer.class);

        Predicate _testIdIs = cb.equal(root.get("id"), participant.getId());
        TypedQuery<Answer> query = em.createQuery(cq.where(_testIdIs));
        return query.getResultList();
    }

    @Override
    public Stream<Participant> getAllConnectedParticipants() {
        TypedQuery<Participant> participantQuery = em.createQuery(
                "SELECT p FROM TestSession t INNER JOIN t.testSessionId p " +
                        "WHERE t.testSessionState=:state",
                Participant.class
        ).setParameter("state", TestSession.State.ACTIVE);
        return participantQuery.getResultStream();
    }

    @Override
    public void add(Participant object) {
        em.persist(object);
    }

    @Override
    public void delete(Participant object) {
        em.remove(object);
    }

    @Override
    public Participant get(Long objKey) {
        return em.find(Participant.class, objKey);
    }

    @Override
    public void update(Participant object) {
        em.merge(object);
    }
}
