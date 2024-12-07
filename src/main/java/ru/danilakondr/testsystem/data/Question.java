package ru.danilakondr.testsystem.data;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.LazyCollection;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Question implements Ownable {
    @Override
    public boolean isOwnedBy(User user) {
        return this.test.isOwnedBy(user);
    }

    @JsonFormat(shape=JsonFormat.Shape.STRING)
    public enum Type {
        NUMBER_ENTRY,
        STRING_ENTRY,
        SINGLE_SELECTION,
        MULTIPLE_SELECTION,
    }

    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE)
    private long id;

    @ManyToOne(optional=false)
    private Test test;

    private Type type;

    private String text;

    @OneToMany(cascade=CascadeType.ALL, mappedBy="question")
    private List<AnswerVariant> variants;
}
