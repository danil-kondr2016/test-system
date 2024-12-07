package ru.danilakondr.testsystem.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.danilakondr.testsystem.data.Answer;
import ru.danilakondr.testsystem.data.Participant;

import java.util.List;

public interface AnswerDAO extends JpaRepository<Answer, Long> {
    List<Answer> findByParticipant(Participant participant);
}
