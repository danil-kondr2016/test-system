package ru.danilakondr.testsystem.jwt;

import io.jsonwebtoken.Claims;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.danilakondr.testsystem.data.User;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class JwtUtils {

    public static JwtAuthentication generate(Claims claims) {
        final JwtAuthentication jwtInfoToken = new JwtAuthentication();
        jwtInfoToken.setRole(claims.get("role", User.Role.class));
        jwtInfoToken.setUserName(claims.getSubject());
        return jwtInfoToken;
    }

}
