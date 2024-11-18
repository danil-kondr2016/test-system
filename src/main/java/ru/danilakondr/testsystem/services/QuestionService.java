package ru.danilakondr.testsystem.services;

import ru.danilakondr.testsystem.data.Question;

public interface QuestionService {
    Question create(long testId, Question.Type type, String text);
    Question get(long questionId);
    void update(Question question);
    void remove(long questionId);
}
