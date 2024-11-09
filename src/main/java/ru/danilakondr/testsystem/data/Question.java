package ru.danilakondr.testsystem.data;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class Question {
    public enum Type {
        NUMBER_ENTRY,
        STRING_ENTRY,
        SINGLE_SELECTION,
        MULTIPLE_SELECTION,
    }

    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE)
    private long questionId;

    @ManyToOne(optional=false)
    private long testId;

    private Type type;

    private String text;

    @OneToMany(cascade=CascadeType.ALL, mappedBy="questionId")
    private List<AnswerVariant> variants;

    public long getQuestionId() {
        return questionId;
    }

    public long getTestId() {
        return testId;
    }

    public Type getType() {
        return type;
    }

    public String getText() {
        return text;
    }

    public void setQuestionId(long questionId) {
        this.questionId = questionId;
    }

    public void setTestId(long testId) {
        this.testId = testId;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public List<AnswerVariant> getVariants() {
        return variants;
    }

    public void setVariants(List<AnswerVariant> variants) {
        this.variants = variants;
    }
}
