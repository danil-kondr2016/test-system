package ru.danilakondr.testsystem.data;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Question {
    @JsonFormat(shape=JsonFormat.Shape.STRING)
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
}
