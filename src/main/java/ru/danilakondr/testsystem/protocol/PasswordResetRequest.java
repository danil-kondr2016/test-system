package ru.danilakondr.testsystem.protocol;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class PasswordResetRequest {
    String resetKey, newPassword;
}
