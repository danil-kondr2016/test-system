package ru.danilakondr.testsystem.data;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.UUID;

import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserSession {
    @Id
    private UUID sessionId;

    @ManyToOne(optional=false)
    @JoinColumn(name="user")
    private User user;

    private LocalDateTime loginDate;

    private LocalDateTime expires;
}