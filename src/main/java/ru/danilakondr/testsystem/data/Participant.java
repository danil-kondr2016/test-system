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
public class Participant {
    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE)
    private long id;

    @ManyToOne(optional=false)
    private TestSession testSession;

    private String name;
}
