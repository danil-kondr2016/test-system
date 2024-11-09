package ru.danilakondr.testsystem.data;

import jakarta.persistence.*;

@Entity
public class AnswerVariant {
    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE)
    private long variantId;

    @ManyToOne(optional=false)
    private long questionId;

    private String text;

    private boolean correct;

    public long getVariantId() {
        return variantId;
    }

    public long getQuestionId() {
        return questionId;
    }

    public String getText() {
        return text;
    }

    public boolean isCorrect() {
        return correct;
    }

    public void setVariantId(long variantId) {
        this.variantId = variantId;
    }

    public void setQuestionId(long questionId) {
        this.questionId = questionId;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setCorrect(boolean correct) {
        this.correct = correct;
    }
}
