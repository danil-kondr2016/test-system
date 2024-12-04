package ru.danilakondr.testsystem.dao;

import ru.danilakondr.testsystem.data.PasswordResetToken;

import java.util.UUID;

public interface PasswordResetTokenDAO extends DataAccessObject<PasswordResetToken, UUID> {
}
