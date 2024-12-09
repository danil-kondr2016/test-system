package ru.danilakondr.testsystem.protocol;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AnswerBody {
    long questionId;
    long variantId;
    String text;
}
