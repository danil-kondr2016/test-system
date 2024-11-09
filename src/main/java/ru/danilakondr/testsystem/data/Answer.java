package ru.danilakondr.testsystem.data;

import jakarta.persistence.*;

@Entity
public class Answer {
    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE)
    private long answerId;

    @ManyToOne(optional=false)
    private long participantId;

    @ManyToOne(optional=false)
    private long questionId;

    private String text;

    @ManyToOne
    private long variantId;

    public long getAnswerId() {
        return answerId;
    }

    public long getParticipantId() {
        return participantId;
    }

    public long getQuestionId() {
        return questionId;
    }

    public String getText() {
        return text;
    }

    public long getVariantId() {
        return variantId;
    }

    public void setAnswerId(long answerId) {
        this.answerId = answerId;
    }

    public void setParticipantId(long participantId) {
        this.participantId = participantId;
    }

    public void setQuestionId(long questionId) {
        this.questionId = questionId;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setVariantId(long variantId) {
        this.variantId = variantId;
    }
}
