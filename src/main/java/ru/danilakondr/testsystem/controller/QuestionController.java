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

@Controller
@RequiredArgsConstructor
public class QuestionController {
    private final QuestionService questionService;
    private final TestService testService;

    // TODO implement get questions for participants
    @GetMapping("/api/question/{id}")
    public ResponseEntity<Response> getQuestionInfo(@PathVariable("id") String questionIdStr) {
        final User currentUser = UserUtils.getCurrentUser();

        long questionId = Long.parseUnsignedLong(questionIdStr);
        Question question = questionService.get(questionId);
        if (question == null)
            return ResponseEntity.notFound().build();
        if (!question.isOwnedBy(currentUser))
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new Response.Error("PERMISSION_DENIED"));

        Description.Question descr = new Description.Question(question);
        return ResponseEntity.ok(new Response.Description(descr));
    }

    @PutMapping("/api/question")
    public ResponseEntity<Response> putQuestion(@RequestBody QuestionBody req, @RequestHeader("Host") String host) {
        Test test = testService.get(req.getTestId());
        if (test == null)
            return ResponseEntity.badRequest().build();

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
    public ResponseEntity<Response> patchQuestion(@RequestBody QuestionBody req, @PathVariable("id") String questionIdStr) {
        final User currentUser = UserUtils.getCurrentUser();

        long questionId = Long.parseUnsignedLong(questionIdStr);
        Question question = questionService.get(questionId);
        if (question == null)
            return ResponseEntity.notFound().build();
        if (!question.isOwnedBy(currentUser))
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new Response.Error("PERMISSION_DENIED"));

        List<AnswerVariant> variants = req.getVariants().stream()
                .map(description -> new AnswerVariant(description.getId(), question, description.getText(), description.isCorrect()))
                .toList();

        questionService.update(question, req.getType(), req.getText(), variants);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/api/question/{id}")
    public ResponseEntity<Response> deleteQuestion(@PathVariable("id") String questionIdStr) {
        final User currentUser = UserUtils.getCurrentUser();

        long questionId = Long.parseUnsignedLong(questionIdStr);
        Question question = questionService.get(questionId);
        if (question == null)
            return ResponseEntity.notFound().build();
        if (!question.isOwnedBy(currentUser))
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new Response.Error("PERMISSION_DENIED"));

        questionService.remove(question);

        return ResponseEntity.noContent().build();
    }
}
