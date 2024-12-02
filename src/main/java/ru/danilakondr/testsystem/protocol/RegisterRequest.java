package ru.danilakondr.testsystem.protocol;

import lombok.*;

@Data
public class RegisterRequest {
    String login, email, password;
}
