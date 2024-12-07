package ru.danilakondr.testsystem.services;

import ru.danilakondr.testsystem.data.AnswerVariant;
import ru.danilakondr.testsystem.data.Question;
import ru.danilakondr.testsystem.data.Test;

import java.util.List;

public interface QuestionService {
    Question create(Test test, Question.Type type, String text, List<AnswerVariant> variants);
    Question get(long questionId);
    void update(Question question);
    void remove(Question question);
}
