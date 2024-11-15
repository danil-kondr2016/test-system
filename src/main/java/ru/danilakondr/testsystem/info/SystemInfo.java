package ru.danilakondr.testsystem.info;

public record SystemInfo(long activeSessionsCount,
                         long activeTestSessionsCount,
                         long totalParticipantsConnected) {}
