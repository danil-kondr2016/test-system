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
public class AnswerVariant {
    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE)
    private long variantId;

    @ManyToOne(optional=false)
    private long questionId;

    private String text;

    private boolean correct;
}
