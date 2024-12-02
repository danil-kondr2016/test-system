package ru.danilakondr.testsystem.protocol;

import lombok.*;

@Data
public class ChangePasswordRequest {
    private String oldPassword, newPassword;
}
