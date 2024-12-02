package ru.danilakondr.testsystem.protocol;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
public class JwtResponse extends Response {
    private String accessToken, refreshToken;
}
