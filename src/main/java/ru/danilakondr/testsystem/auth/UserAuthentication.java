package ru.danilakondr.testsystem.auth;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import ru.danilakondr.testsystem.data.UserSession;

import java.util.Collection;

@Getter
@Setter
public class UserAuthentication implements Authentication {
    private boolean authenticated;
    private UserSession principal;

    public UserAuthentication(UserSession session) {
        this.principal = session;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() { return principal.getUser().getAuthorities(); }

    @Override
    public Object getCredentials() { return null; }

    @Override
    public Object getDetails() { return null; }

    @Override
    public boolean isAuthenticated() { return authenticated; }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        this.authenticated = isAuthenticated;
    }

    @Override
    public String getName() { return principal.getUser().getUsername(); }

}