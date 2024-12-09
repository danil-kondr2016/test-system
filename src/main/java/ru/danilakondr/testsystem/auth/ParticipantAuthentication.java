package ru.danilakondr.testsystem.auth;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import ru.danilakondr.testsystem.data.Participant;

import java.util.Collection;
import java.util.List;

@Getter
@Setter
public class ParticipantAuthentication implements Authentication {
    private boolean authenticated;
    private Participant principal;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("PARTICIPANT"));
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getDetails() {
        return null;
    }

    @Override
    public String getName() {
        return principal.getName();
    }
}
