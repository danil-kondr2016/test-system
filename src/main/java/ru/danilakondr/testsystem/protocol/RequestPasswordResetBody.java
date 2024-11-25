package ru.danilakondr.testsystem.protocol;

import lombok.Data;

@Data
public class RequestPasswordResetBody {
    String email;
}
