package ru.danilakondr.testsystem.protocol;

import lombok.*;

@Data
public class RegisterBody {
    String login, email, password;
}
