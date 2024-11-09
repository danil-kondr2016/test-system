package ru.danilakondr.testsystem.data;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
public class UserSession {
    @Id
    @GeneratedValue(strategy=GenerationType.UUID)
    private UUID sessionId;

    @ManyToOne(optional=false)
    @JoinColumn(name="userId")
    private long userId;

    private LocalDateTime loginDate;

    public UUID getSessionId() {
        return sessionId;
    }

    public void setSessionId(UUID sessionId) {
        this.sessionId = sessionId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public LocalDateTime getLoginDate() {
        return loginDate;
    }

    public void setLoginDate(LocalDateTime loginDate) {
        this.loginDate = loginDate;
    }
}
