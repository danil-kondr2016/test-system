package ru.danilakondr.testsystem.data;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
public class Participant {
    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE)
    private long participantId;

    @ManyToOne(optional=false)
    private UUID testSessionId;

    private String name;

    public long getParticipantId() {
        return participantId;
    }

    public UUID getTestSessionId() {
        return testSessionId;
    }

    public String getName() {
        return name;
    }

    public void setParticipantId(long participantId) {
        this.participantId = participantId;
    }

    public void setTestSessionId(UUID testSessionId) {
        this.testSessionId = testSessionId;
    }

    public void setName(String name) {
        this.name = name;
    }
}
