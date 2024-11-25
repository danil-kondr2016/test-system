package ru.danilakondr.testsystem.protocol;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import ru.danilakondr.testsystem.data.Question;

@Data
public class QuestionDescription {
    long questionId;
    Question.Type type;
}
