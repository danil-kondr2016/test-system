package ru.danilakondr.testsystem.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.danilakondr.testsystem.data.PasswordResetToken;

import java.util.UUID;

public interface PasswordResetTokenDAO extends JpaRepository<PasswordResetToken, UUID> {
}
