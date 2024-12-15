package ru.danilakondr.testsystem.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.danilakondr.testsystem.data.*;
import ru.danilakondr.testsystem.protocol.QuestionBody;
import ru.danilakondr.testsystem.protocol.Description;
import ru.danilakondr.testsystem.protocol.Response;
import ru.danilakondr.testsystem.services.QuestionService;
import ru.danilakondr.testsystem.services.TestService;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class QuestionController {
    private final QuestionService questionService;
    private final TestService testService;

    @GetMapping("/api/question/{id}")
    public ResponseEntity<Response> getQuestionInfo(@PathVariable String id) {
        long questionId = Long.parseUnsignedLong(id);
        Question question = questionService.get(questionId);

        final Optional<User> currentUser = UserUtils.getCurrentUser();
        if (currentUser.isPresent()) {
            if (!question.isOwnedBy(currentUser.get()))
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Response.Error.PERMISSION_DENIED);
        }
        else {
            Optional<Participant> participant = UserUtils.getCurrentParticipant();
            if (participant.isEmpty())
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Response.Error.PERMISSION_DENIED);

            Test test = participant.get().getTestSession().getTest();
            if (question.getTest().getId() != test.getId())
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Response.Error.PERMISSION_DENIED);
        }

        Description.Question descr = new Description.Question(question);
        return ResponseEntity.ok(new Response.Description(descr));
    }

    @PutMapping("/api/question")
    public ResponseEntity<Response> putQuestion(@RequestBody QuestionBody req, @RequestHeader("Host") String host) {
        final Optional<User> currentUser = UserUtils.getCurrentUser();
        if (currentUser.isEmpty())
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Response.Error.PERMISSION_DENIED);

        Test test = testService.get(req.getTestId());
        if (!test.isOwnedBy(currentUser.get()))
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Response.Error.PERMISSION_DENIED);

        List<AnswerVariant> variants = req.getVariants().stream()
                .map(
                    description ->
                            new AnswerVariant(description.getId(),
                                    null,
                                    description.getText(),
                                    description.isCorrect())
                )
                .toList();
        Question question = questionService.create(test, req.getType(), req.getText(), variants);
        return ResponseEntity.created(URI.create("http://"+host+"/api/question/"+question.getId())).build();
    }

    @PatchMapping("/api/question/{id}")
    public ResponseEntity<Response> patchQuestion(@RequestBody QuestionBody req, @PathVariable String id) {
        final Optional<User> currentUser = UserUtils.getCurrentUser();
        if (currentUser.isEmpty())
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Response.Error.PERMISSION_DENIED);

        long questionId = Long.parseUnsignedLong(id);
        Question question = questionService.get(questionId);
        if (!question.isOwnedBy(currentUser.get()))
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Response.Error.PERMISSION_DENIED);

        List<AnswerVariant> variants = req.getVariants().stream()
                .map(description -> new AnswerVariant(description.getId(), question, description.getText(), description.isCorrect()))
                .toList();

        questionService.update(question, req.getType(), req.getText(), variants);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/api/question/{id}")
    public ResponseEntity<Response> deleteQuestion(@PathVariable String id) {
        final Optional<User> currentUser = UserUtils.getCurrentUser();
        if (currentUser.isEmpty())
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Response.Error.PERMISSION_DENIED);

        long questionId = Long.parseUnsignedLong(id);
        Question question = questionService.get(questionId);
        if (!question.isOwnedBy(currentUser.get()))
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Response.Error.PERMISSION_DENIED);

        questionService.remove(question);

        return ResponseEntity.noContent().build();
    }
}
