package ru.danilakondr.testsystem.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.danilakondr.testsystem.data.Answer;
import ru.danilakondr.testsystem.data.Participant;

import java.util.UUID;
import java.util.stream.Stream;

public interface ParticipantDAO extends JpaRepository<Participant, UUID> {
    @Query("SELECT p FROM Participant p INNER JOIN p.testSession t WHERE t.testSessionState=ACTIVE")
    Stream<Participant> findAllConnectedParticipants();
}
