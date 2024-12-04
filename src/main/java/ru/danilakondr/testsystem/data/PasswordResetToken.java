package ru.danilakondr.testsystem.data;

import com.github.f4b6a3.uuid.UuidCreator;
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
public class PasswordResetToken {
    @Id
    private UUID resetTokenId;

    @ManyToOne(optional=false)
    @JoinColumn(name="user")
    private User user;

    private LocalDateTime expires;

    public PasswordResetToken(User user) {
        resetTokenId = UuidCreator.getRandomBased();
        this.user = user;
        this.expires = LocalDateTime.now().plusSeconds(86400L);
    }
}