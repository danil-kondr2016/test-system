package ru.danilakondr.testsystem.protocol;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse extends Response {
    private String reason;
}
