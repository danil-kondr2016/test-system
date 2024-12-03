package ru.danilakondr.testsystem.protocol;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
public class SessionKeyResponse extends Response {
    private String sessionKey;
}
