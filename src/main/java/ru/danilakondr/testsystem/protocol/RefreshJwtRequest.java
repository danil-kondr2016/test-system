package ru.danilakondr.testsystem.protocol;

import lombok.Data;

@Data
public class RefreshJwtRequest {
    private String refreshToken;
}
