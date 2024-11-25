package ru.danilakondr.testsystem.data;

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
public class UserSession {
    @Id
    @GeneratedValue(strategy=GenerationType.UUID)
    private UUID sessionId;

    @ManyToOne(optional=false)
    @JoinColumn(name="userId")
    private long userId;

    private LocalDateTime loginDate;
}
