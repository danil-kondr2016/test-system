package ru.danilakondr.testsystem.info;

import java.util.ArrayList;
import java.util.List;

public class Report {
    public record Answer(long testId,
                         long questionId,
                         String questionText,
                         long answerVariantId,
                         String answerVariantText,
                         boolean correct) {}

    private final long participantId;
    private final String participantName;
    private final List<Answer> answers;

    public Report(long participantId, String participantName) {
        answers = new ArrayList<>();
        this.participantId = participantId;
        this.participantName = participantName;
    }

    public long getParticipantId() {
        return participantId;
    }

    public String getParticipantName() {
        return participantName;
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
