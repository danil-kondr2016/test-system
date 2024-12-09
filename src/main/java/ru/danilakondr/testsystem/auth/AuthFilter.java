package ru.danilakondr.testsystem.auth;

import com.github.f4b6a3.uuid.UuidCreator;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;
import ru.danilakondr.testsystem.dao.UserDAO;
import ru.danilakondr.testsystem.dao.UserSessionDAO;
import ru.danilakondr.testsystem.data.Participant;
import ru.danilakondr.testsystem.data.User;
import ru.danilakondr.testsystem.data.UserSession;
import ru.danilakondr.testsystem.exception.InvalidCredentialsException;
import ru.danilakondr.testsystem.services.ParticipantService;
import ru.danilakondr.testsystem.services.UserService;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Component
public class AuthFilter extends GenericFilterBean {
    private static final String AUTHORIZATION = "Authorization";

    @Autowired
    private UserService service;

    @Autowired
    private ParticipantService participantService;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        final String tokenString = getTokenFromString((HttpServletRequest) servletRequest);
        if (tokenString != null) {
            if (tokenString.startsWith("PART:")) {
                final UUID uuidParticipantId = UuidCreator.fromString(tokenString.substring(4));
                final Optional<Participant> participant = participantService.get(uuidParticipantId);
                if (participant.isEmpty())
                    throw new InvalidCredentialsException("INVALID_CREDENTIALS");

                ParticipantAuthentication authentication = new ParticipantAuthentication();
                authentication.setPrincipal(participant.get());
                authentication.setAuthenticated(true);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
            else {
                final UUID uuidToken = UuidCreator.fromString(tokenString);
                final Optional<UserSession> session = service.authenticate(uuidToken);
                if (session.isEmpty()) {
                    throw new InvalidCredentialsException("INVALID_CREDENTIALS");
                }

                UserAuthentication authentication = new UserAuthentication(session.get());
                authentication.setAuthenticated(true);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }

    private String getTokenFromString(HttpServletRequest request) {
        final String authorization = request.getHeader(AUTHORIZATION);
        if (StringUtils.hasText(authorization) && authorization.startsWith("Bearer ")) {
            return authorization.substring(7).trim();
        }
        return null;
    }
}
