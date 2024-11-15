package ru.danilakondr.testsystem.dao;

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
    private EntityManagerFactory entityManagerFactory;

    @Autowired
    public void setEntityManagerFactory(jakarta.persistence.EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }

    @Override
    public void add(UserSession object) {
        EntityManager em = entityManagerFactory.createEntityManager();
        em.persist(object);
    }

    @Override
    public void delete(UserSession object) {
        EntityManager em = entityManagerFactory.createEntityManager();
        em.remove(object);
    }

    @Override
    public UserSession get(UUID objKey) {
        EntityManager em = entityManagerFactory.createEntityManager();
        return em.find(UserSession.class, objKey);
    }

    @Override
    public void update(UserSession object) {
        EntityManager em = entityManagerFactory.createEntityManager();
        em.merge(object);
    }

    @Override
    public Stream<UserSession> getAllUserSessions(UserSession auth) {
        EntityManager em = entityManagerFactory.createEntityManager();

        // Check if user exists and is administrator
        TypedQuery<User> userQuery = em.createQuery("FROM User WHERE userId=:id", User.class);
        userQuery.setParameter("id", auth.getUserId());
        User user = userQuery.getSingleResult();
        if (user.getUserRole() != User.Role.ADMINISTRATOR)
            throw new RuntimeException("Permission denied");

        TypedQuery<UserSession> userSessionQuery = em.createQuery("FROM UserSession", UserSession.class);
        return userSessionQuery.getResultStream();
    }
}
