package ru.danilakondr.testsystem.info;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Report {
    public record Answer(long testId,
                         long questionId,
                         String questionText,
                         long answerVariantId,
                         String answerVariantText,
                         boolean correct) {}

    @Getter
    private final UUID participantId;
    @Getter
    private final String participantName;
    private final List<Answer> answers;

    public Report(UUID participantId, String participantName) {
        answers = new ArrayList<>();
        this.participantId = participantId;
        this.participantName = participantName;
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
