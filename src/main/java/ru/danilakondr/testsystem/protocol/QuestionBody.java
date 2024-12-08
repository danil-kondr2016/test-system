package ru.danilakondr.testsystem.protocol;

import lombok.Data;
import ru.danilakondr.testsystem.data.Question;

import java.util.List;

@Data
public class QuestionBody {
    long testId;
    Question.Type type;
    String text;
    List<Description.AnswerVariant> variants;
}
