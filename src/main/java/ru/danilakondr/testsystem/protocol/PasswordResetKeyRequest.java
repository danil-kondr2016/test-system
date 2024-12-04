package ru.danilakondr.testsystem.protocol;

import lombok.Data;

@Data
public class PasswordResetKeyRequest {
    String email;
}
