package ru.danilakondr.testsystem.services;

import ru.danilakondr.testsystem.data.Question;
import ru.danilakondr.testsystem.data.Test;

public interface QuestionService {
    Question create(Test test, Question.Type type, String text);
    Question get(long questionId);
    void update(Question question);
    void remove(Question question);
}
