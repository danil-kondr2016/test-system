package ru.danilakondr.testsystem.services;

import io.jsonwebtoken.Claims;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import ru.danilakondr.testsystem.exception.InvalidCredentialsException;
import ru.danilakondr.testsystem.jwt.JwtAuthentication;
import ru.danilakondr.testsystem.protocol.AuthorizeRequest;
import ru.danilakondr.testsystem.protocol.JwtResponse;
import ru.danilakondr.testsystem.data.User;
import ru.danilakondr.testsystem.jwt.JwtProvider;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserService userService;
    private final Map<String, String> refreshStorage = new HashMap<>();
    private final JwtProvider jwtProvider;

    public JwtResponse login(@NonNull AuthorizeRequest authRequest) {
        final User user = userService.find(authRequest.getLogin())
                .orElseThrow(() -> new InvalidCredentialsException("INVALID_CREDENTIALS"));
        if (user.getPassword().equals(authRequest.getPassword())) {
            final String accessToken = jwtProvider.generateAccessToken(user);
            final String refreshToken = jwtProvider.generateRefreshToken(user);
            refreshStorage.put(user.getLogin(), refreshToken);
            return new JwtResponse(accessToken, refreshToken);
        } else {
            throw new InvalidCredentialsException("INVALID_CREDENTIALS");
        }
    }

    public JwtResponse getAccessToken(@NonNull String refreshToken) {
        if (jwtProvider.validateRefreshToken(refreshToken)) {
            final Claims claims = jwtProvider.getRefreshClaims(refreshToken);
            final String login = claims.getSubject();
            final String saveRefreshToken = refreshStorage.get(login);
            if (saveRefreshToken != null && saveRefreshToken.equals(refreshToken)) {
                final User user = userService.find(login)
                        .orElseThrow(() -> new InvalidCredentialsException("INVALID_CREDENTIALS"));
                final String accessToken = jwtProvider.generateAccessToken(user);
                return new JwtResponse(accessToken, null);
            }
        }
        return new JwtResponse(null, null);
    }

    public JwtResponse refresh(@NonNull String refreshToken) {
        if (jwtProvider.validateRefreshToken(refreshToken)) {
            final Claims claims = jwtProvider.getRefreshClaims(refreshToken);
            final String login = claims.getSubject();
            final String saveRefreshToken = refreshStorage.get(login);
            if (saveRefreshToken != null && saveRefreshToken.equals(refreshToken)) {
                final User user = userService.find(login)
                        .orElseThrow(() -> new InvalidCredentialsException("INVALID_CREDENTIALS"));
                final String accessToken = jwtProvider.generateAccessToken(user);
                final String newRefreshToken = jwtProvider.generateRefreshToken(user);
                refreshStorage.put(user.getLogin(), newRefreshToken);
                return new JwtResponse(accessToken, newRefreshToken);
            }
        }
        throw new InvalidCredentialsException("INVALID_CREDENTIALS");
    }

    public JwtAuthentication getAuthInfo() {
        return (JwtAuthentication) SecurityContextHolder.getContext().getAuthentication();
    }

}
