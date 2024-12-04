package ru.danilakondr.testsystem.dao;

import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.danilakondr.testsystem.data.PasswordResetToken;

import java.util.UUID;

@Repository
public class PasswordResetTokenDAOImpl implements PasswordResetTokenDAO {
    @Autowired
    private EntityManager em;

    @Override
    public void add(PasswordResetToken object) {
        em.persist(object);
    }

    @Override
    public void delete(PasswordResetToken object) {
        em.remove(object);
    }

    @Override
    public PasswordResetToken get(UUID objKey) {
        return em.find(PasswordResetToken.class, objKey);
    }

    @Override
    public void update(PasswordResetToken object) {
        em.merge(object);
    }
}
