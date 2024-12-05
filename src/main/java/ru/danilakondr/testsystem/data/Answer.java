package ru.danilakondr.testsystem.data;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Answer {
    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE)
    private long id;

    @ManyToOne(optional=false)
    private Participant participant;

    @ManyToOne(optional=false)
    private Question question;

    private String text;

    @ManyToOne(optional = true)
    private AnswerVariant variant;
}
