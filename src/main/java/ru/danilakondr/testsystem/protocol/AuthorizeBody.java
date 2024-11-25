package ru.danilakondr.testsystem.protocol;

import lombok.Data;

@Data
public class AuthorizeBody {
    String login, password;
}
