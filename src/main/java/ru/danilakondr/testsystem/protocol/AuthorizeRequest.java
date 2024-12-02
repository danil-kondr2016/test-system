package ru.danilakondr.testsystem.protocol;

import lombok.*;

@Data
public class AuthorizeRequest {
    private String login, password;
}
