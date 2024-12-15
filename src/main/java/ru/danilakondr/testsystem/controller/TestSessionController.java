package ru.danilakondr.testsystem.controller;

import com.github.f4b6a3.uuid.UuidCreator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.danilakondr.testsystem.data.Participant;
import ru.danilakondr.testsystem.data.Test;
import ru.danilakondr.testsystem.data.TestSession;
import ru.danilakondr.testsystem.data.User;
import ru.danilakondr.testsystem.protocol.Description;
import ru.danilakondr.testsystem.protocol.Response;
import ru.danilakondr.testsystem.protocol.TestSessionBody;
import ru.danilakondr.testsystem.services.ParticipantService;
import ru.danilakondr.testsystem.services.TestService;
import ru.danilakondr.testsystem.services.TestSessionService;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
public class TestSessionController {
    private final ParticipantService participantService;
    private final TestSessionService testSessionService;
    private final TestService testService;

    @GetMapping("/api/testSession/{id}")
    public ResponseEntity<Response> getTestSessionInfo(@PathVariable String id) {
        UUID sessionId = UuidCreator.fromString(id);
        TestSession session = testSessionService.get(sessionId);

        final Optional<User> user = UserUtils.getCurrentUser();
        if (user.isPresent()) {
            if (!session.isOwnedBy(user.get()))
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Response.Error.PERMISSION_DENIED);
        }
        else {
            final Optional<Participant> participant = UserUtils.getCurrentParticipant();
            if (participant.isEmpty())
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Response.Error.PERMISSION_DENIED);
            if (participant.get().getTestSession().getId().compareTo(sessionId) != 0)
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Response.Error.PERMISSION_DENIED);
        }

        Description.TestSession description = new Description.TestSession(session);
        return ResponseEntity.ok(new Response.Description(description));
    }

    @PutMapping("/api/testSession")
    public ResponseEntity<Response> putTestSession(@RequestBody TestSessionBody request, @RequestHeader("Host") String host) {
        final Optional<User> user = UserUtils.getCurrentUser();
        if (user.isEmpty())
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Response.Error.PERMISSION_DENIED);

        Test test = testService.get(request.getTestId());
        if (!test.isOwnedBy(user.get()))
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Response.Error.PERMISSION_DENIED);

        TestSession session = testSessionService.create(test);
        return ResponseEntity.created(URI.create("http://"+host+"/api/testSession/"+session.getId().toString())).build();
    }

    @GetMapping("/api/testSession/{id}/participants")
    public ResponseEntity<Response> getParticipants(@PathVariable String id) {
        final Optional<User> user = UserUtils.getCurrentUser();
        if (user.isEmpty())
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Response.Error.PERMISSION_DENIED);

        UUID sessionId = UuidCreator.fromString(id);
        TestSession session = testSessionService.get(sessionId);
        if (!session.isOwnedBy(user.get()))
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Response.Error.PERMISSION_DENIED);

        List<Description.Participant> descriptors = participantService.getByTestSession(session)
                .stream()
                .map(Description.Participant::new)
                .toList();

        return ResponseEntity.ok(new Response.DescriptionList(descriptors));
    }

    @PostMapping("/api/testSession/{id}/complete")
    public ResponseEntity<Response> completeTestSession(@PathVariable String id) {
        final Optional<User> user = UserUtils.getCurrentUser();
        if (user.isEmpty())
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Response.Error.PERMISSION_DENIED);

        UUID sessionId = UuidCreator.fromString(id);
        TestSession session = testSessionService.get(sessionId);
        if (!session.isOwnedBy(user.get()))
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Response.Error.PERMISSION_DENIED);

        session.setTestSessionState(TestSession.State.COMPLETED);

        return ResponseEntity.noContent().build();
    }
}
