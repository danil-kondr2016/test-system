package ru.danilakondr.testsystem.protocol;

import lombok.Data;

@Data
public class ChangePasswordBody {
    String oldPassword, newPassword;
}
