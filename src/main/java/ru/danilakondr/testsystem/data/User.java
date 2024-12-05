package ru.danilakondr.testsystem.data;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.Map;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE)
    @Column(nullable = false)
    private long id;

    @Column(nullable = false, unique = true)
    private String login;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    @Builder.Default
    private Role userRole = Role.ORGANIZATOR;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return userRole.getAuthorities();
    }

    @Override
    public String getUsername() {
        return login;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @JsonFormat(shape=JsonFormat.Shape.STRING)
    public enum Role {
        ORGANIZATOR(List.of(new SimpleGrantedAuthority("ORGANIZATOR"))),
        ADMINISTRATOR(List.of(new SimpleGrantedAuthority("ORGANIZATOR"), new SimpleGrantedAuthority("ADMINISTRATOR"))),
        ;

        @JsonIgnore
        private transient Collection<SimpleGrantedAuthority> authorities;

        @JsonIgnore
        private static final Map<String, Role> values = Map.of(
                ORGANIZATOR.name(), ORGANIZATOR,
                ADMINISTRATOR.name(), ADMINISTRATOR);

        public static Role fromName(String name) {
            return values.get(name);
        }

        public Collection<SimpleGrantedAuthority> getAuthorities() {
            return authorities;
        }

        private Role(Collection<SimpleGrantedAuthority> authorities) {
            this.authorities = authorities;
        }
    }
}
