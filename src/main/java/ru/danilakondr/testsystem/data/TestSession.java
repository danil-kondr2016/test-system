package ru.danilakondr.testsystem.data;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
public class TestSession {
    public enum State {
        NOT_ACTIVE,
        ACTIVE,
        COMPLETED,
    }
    @Id
    @GeneratedValue(strategy= GenerationType.UUID)
    private UUID testSessionId;

    @ManyToOne
    private long testId;

    private State testSessionState;

    private LocalDateTime begin;

    private LocalDateTime end;

    public UUID getTestSessionId() {
        return testSessionId;
    }

    public long getTestId() {
        return testId;
    }

    public State getTestSessionState() {
        return testSessionState;
    }

    public LocalDateTime getBegin() {
        return begin;
    }

    public LocalDateTime getEnd() {
        return end;
    }

    public void setTestSessionId(UUID testSessionId) {
        this.testSessionId = testSessionId;
    }

    public void setTestId(long testId) {
        this.testId = testId;
    }

    public void setTestSessionState(State testSessionState) {
        this.testSessionState = testSessionState;
    }

    public void setBegin(LocalDateTime begin) {
        this.begin = begin;
    }

    public void setEnd(LocalDateTime end) {
        this.end = end;
    }
}
