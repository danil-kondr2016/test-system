package ru.danilakondr.testsystem.controller;

import com.github.f4b6a3.uuid.UuidCreator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.danilakondr.testsystem.data.*;
import ru.danilakondr.testsystem.protocol.AnswerBody;
import ru.danilakondr.testsystem.protocol.Description;
import ru.danilakondr.testsystem.protocol.ParticipantRegisterBody;
import ru.danilakondr.testsystem.protocol.Response;
import ru.danilakondr.testsystem.services.ParticipantService;
import ru.danilakondr.testsystem.services.QuestionService;
import ru.danilakondr.testsystem.services.TestSessionService;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
public class ParticipantController {
    private final TestSessionService testSessionService;
    private final ParticipantService participantService;
    private final QuestionService questionService;

    @PutMapping("/api/participant")
    public ResponseEntity<Response> putParticipant(@RequestBody ParticipantRegisterBody request, @RequestHeader("Host") String host) {
        TestSession testSession = testSessionService.get(request.getTestSessionId());
        Participant participant = participantService.create(testSession, request.getName());

        Test test = testSession.getTest();
        List<Description.Question> questions = test.getQuestions().stream().map(Description.Question::new).toList();

        Response.Participant response = new Response.Participant("PART:"+participant.getId().toString(), questions);
        return ResponseEntity.created(URI.create("http://" + host + "/api/participant/" + participant.getId().toString())).body(response);
    }

    @GetMapping("/api/participant/{id}")
    public ResponseEntity<Response> getParticipantInfo(@PathVariable String id) {
        final Optional<User> currentUser = UserUtils.getCurrentUser();
        if (currentUser.isEmpty())
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Response.Error.PERMISSION_DENIED);

        UUID participantId = UuidCreator.fromString(id);
        Optional<Participant> participant = participantService.get(participantId);
        if (participant.isEmpty())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Response.Error.RESOURCE_NOT_FOUND);
        if (!participant.get().getTestSession().isOwnedBy(currentUser.get()))
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Response.Error.PERMISSION_DENIED);
        if (!participantService.validate(participant.get()))
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Response.Error.PERMISSION_DENIED);

        Response.Description response = new Response.Description(new Description.Participant(participant.get()));
        return ResponseEntity.ok(response);
    }

    @PutMapping("/api/participant/{id}/answer")
    public ResponseEntity<Response> putAnswer(@PathVariable String id, @RequestBody AnswerBody request) {
        final Optional<Participant> participant = UserUtils.getCurrentParticipant();
        if (participant.isEmpty())
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Response.Error.PERMISSION_DENIED);
        if (!participantService.validate(participant.get()))
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Response.Error.PERMISSION_DENIED);

        Question question = questionService.get(request.getQuestionId());
        AnswerVariant variant;
        Answer answer = new Answer();

        Optional<AnswerVariant> optVariant = question.getVariants().stream()
                .filter(v -> (v.getId() == request.getVariantId()) || (v.getText().compareToIgnoreCase(request.getText()) == 0))
                .findFirst();
        optVariant.ifPresent(answer::setVariant);

        answer.setParticipant(participant.get());
        answer.setText(request.getText());
        answer.setQuestion(question);
        participantService.putAnswer(participant.get(), answer);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/api/participant/complete")
    public ResponseEntity<Response> complete() {
        final Optional<Participant> participant = UserUtils.getCurrentParticipant();
        if (participant.isEmpty())
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Response.Error.PERMISSION_DENIED);
        if (!participantService.validate(participant.get()))
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Response.Error.PERMISSION_DENIED);

        participantService.complete(participant.get().getId());
        return ResponseEntity.noContent().build();
    }
}
