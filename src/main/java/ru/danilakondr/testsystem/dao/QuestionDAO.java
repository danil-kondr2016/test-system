package ru.danilakondr.testsystem.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.danilakondr.testsystem.data.Question;
import ru.danilakondr.testsystem.data.Test;

import java.util.List;

public interface QuestionDAO extends JpaRepository<Question, Long> {
    List<Question> getByTest(Test test);
}
