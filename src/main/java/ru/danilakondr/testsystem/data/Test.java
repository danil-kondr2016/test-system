package ru.danilakondr.testsystem.data;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class Test {
    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE)
    private long testId;

    @ManyToOne(optional = false)
    @JoinColumn(name="userId")
    private long organizatorId;

    private String name;

    public long getTestId() {
        return testId;
    }

    public long getOrganizatorId() {
        return organizatorId;
    }

    public String getName() {
        return name;
    }

    public void setTestId(long testId) {
        this.testId = testId;
    }

    public void setOrganizatorId(long organizatorId) {
        this.organizatorId = organizatorId;
    }

    public void setName(String name) {
        this.name = name;
    }
}
