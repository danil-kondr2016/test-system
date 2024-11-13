package ru.danilakondr.testsystem.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.danilakondr.testsystem.data.UserSession;

import java.util.UUID;

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
}
