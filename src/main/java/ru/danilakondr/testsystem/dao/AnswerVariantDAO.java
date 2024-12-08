package ru.danilakondr.testsystem.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.danilakondr.testsystem.data.AnswerVariant;

public interface AnswerVariantDAO extends JpaRepository<AnswerVariant, Long> {
}
