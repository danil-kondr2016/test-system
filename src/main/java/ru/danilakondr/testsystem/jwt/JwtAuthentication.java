package ru.danilakondr.testsystem.jwt;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import ru.danilakondr.testsystem.data.User;

import java.util.Collection;

@Getter
@Setter
public class JwtAuthentication implements Authentication {
    private boolean authenticated;
    private String userName;
    private User.Role role;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() { return role.getAuthorities(); }

    @Override
    public Object getCredentials() { return null; }

    @Override
    public Object getDetails() { return null; }

    @Override
    public Object getPrincipal() { return userName; }

    @Override
    public boolean isAuthenticated() { return authenticated; }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        this.authenticated = isAuthenticated;
    }

    @Override
    public String getName() { return userName; }

}