package ru.danilakondr.testsystem.jwt;

import io.jsonwebtoken.Claims;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.danilakondr.testsystem.data.User;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class JwtUtils {

    public static JwtAuthentication generate(Claims claims) {
        final JwtAuthentication jwtInfoToken = new JwtAuthentication();
        jwtInfoToken.setRole(User.Role.fromName((String) claims.get("role")));
        jwtInfoToken.setUserName(claims.getSubject());
        return jwtInfoToken;
    }

}
