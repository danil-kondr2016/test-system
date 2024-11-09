package ru.danilakondr.testsystem.dao;

import ru.danilakondr.testsystem.data.Question;
import ru.danilakondr.testsystem.data.Test;

import java.util.List;

public interface TestDAO extends DataAccessObject<Test, Long> {
    List<Question> getQuestions(Test test);
}
