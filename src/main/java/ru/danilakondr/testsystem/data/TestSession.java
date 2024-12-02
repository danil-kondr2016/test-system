package ru.danilakondr.testsystem.data;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TestSession {
    @JsonFormat(shape=JsonFormat.Shape.STRING)
    public enum State {
        NOT_ACTIVE,
        ACTIVE,
        COMPLETED,
    }
    @Id
    @GeneratedValue(strategy= GenerationType.UUID)
    private UUID testSessionId;

    @ManyToOne
    private Test test;

    private State testSessionState;

    private LocalDateTime begin;

    private LocalDateTime end;
}
