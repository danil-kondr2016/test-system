package ru.danilakondr.testsystem.dao;

import com.github.f4b6a3.uuid.UuidCreator;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.TypedQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.danilakondr.testsystem.data.User;
import ru.danilakondr.testsystem.data.UserSession;

import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

@Repository
public class UserSesssionDAOImpl implements UserSessionDAO {
    @Autowired
    private EntityManager em;

    @Override
    public void add(UserSession object) {
        object.setSessionId(UuidCreator.getRandomBased());
        em.persist(object);
    }

    @Override
    public void delete(UserSession object) {
        em.remove(object);
    }

    @Override
    public UserSession get(UUID objKey) {
        return em.find(UserSession.class, objKey);
    }

    @Override
    public void update(UserSession object) {
        em.merge(object);
    }

    @Override
    public Stream<UserSession> getAllUserSessions() {
        TypedQuery<UserSession> userSessionQuery = em.createQuery("FROM UserSession", UserSession.class);
        return userSessionQuery.getResultStream();
    }
}
