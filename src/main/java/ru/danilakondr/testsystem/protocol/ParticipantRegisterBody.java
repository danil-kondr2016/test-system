package ru.danilakondr.testsystem.protocol;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class ParticipantRegisterBody {
    UUID testSessionId;
    String name;
}
