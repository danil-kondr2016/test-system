package ru.danilakondr.testsystem.data;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Participant {
    public enum State {
        ACTIVE,
        COMPLETED
    }

    @Id
    private UUID id;

    @ManyToOne(optional=false)
    private TestSession testSession;

    private String name;

    private State state;
}
