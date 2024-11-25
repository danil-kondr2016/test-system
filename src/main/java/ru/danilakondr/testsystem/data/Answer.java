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
    private long answerId;

    @ManyToOne(optional=false)
    private long participantId;

    @ManyToOne(optional=false)
    private long questionId;

    private String text;

    @ManyToOne
    private long variantId;
}
