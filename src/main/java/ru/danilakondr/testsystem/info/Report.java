package ru.danilakondr.testsystem.info;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Report {
    public record Answer(@JsonSerialize(as=String.class) long questionId,
                         String questionText,
                         @JsonSerialize(as=String.class) long answerVariantId,
                         String answerVariantText,
                         boolean correct) {}

    @Getter
    private final UUID participantId;
    @Getter
    private final String participantName;
    @Getter
    @JsonSerialize(as=String.class)
    private final long testId;
    @Getter
    private final String testName;

    private final List<Answer> answers;

    public Report(UUID participantId, String participantName, long testId, String testName) {
        answers = new ArrayList<>();
        this.participantId = participantId;
        this.participantName = participantName;
        this.testId = testId;
        this.testName = testName;
    }

    public Answer getAnswer(int index) {
        return answers.get(index);
    }

    public void putAnswer(Answer answer) {
        answers.add(answer);
    }

    public int getAnswerCount(int index) {
        return answers.size();
    }
}
