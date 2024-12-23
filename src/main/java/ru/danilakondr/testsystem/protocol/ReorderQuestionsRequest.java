package ru.danilakondr.testsystem.protocol;

import lombok.Data;

import java.util.List;

@Data
public class ReorderQuestionsRequest {
    List<String> questionIds;
}