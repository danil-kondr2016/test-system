package ru.danilakondr.testsystem.data;

import jakarta.persistence.*;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.UUID;

import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserSession implements Principal {
    @Id
    @GeneratedValue(strategy=GenerationType.UUID)
    private UUID id;

    @ManyToOne(optional=false)
    @JoinColumn(name="user")
    private User user;

    private LocalDateTime loginDate;

    private LocalDateTime expires;

    @Override
    public String getName() {
        return user.getUsername();
    }
}
