package ru.danilakondr.testsystem.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.danilakondr.testsystem.data.Question;
import ru.danilakondr.testsystem.data.Test;
import ru.danilakondr.testsystem.data.User;
import ru.danilakondr.testsystem.data.UserSession;
import ru.danilakondr.testsystem.protocol.CreateTestRequest;
import ru.danilakondr.testsystem.protocol.Response;
import ru.danilakondr.testsystem.protocol.TestDescription;
import ru.danilakondr.testsystem.protocol.TestDescriptionResponse;
import ru.danilakondr.testsystem.services.TestService;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class TestController {
    private final TestService testService;

    @GetMapping("/api/test/{id}")
    public ResponseEntity<Response> getTestInfo(@PathVariable("id") String idString) {
        long testId = Long.parseUnsignedLong(idString);
        Test test = testService.get(testId);
        if (test == null) {
            return ResponseEntity.notFound().build();
        }

        TestDescriptionResponse response = new TestDescriptionResponse();
        response.setName(test.getName());
        List<Long> questionIds = new ArrayList<>();
        for (Question q : test.getQuestions())
            questionIds.add(q.getQuestionId());
        response.setQuestions(questionIds);

        return ResponseEntity.ok(response);
    }

    @PutMapping("/api/test")
    public ResponseEntity<Response> createTest(@RequestBody CreateTestRequest request, @RequestHeader("Host") String host) {
        final Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        final UserSession principal = (UserSession) auth.getPrincipal();
        final User testOwner = principal.getUser();

        Test test = testService.create(testOwner, request.getName());
        return ResponseEntity.created(URI.create("http://"+host+"/api/test/"+test.getTestId())).build();
    }

    @PatchMapping("/api/test/{id}")
    public ResponseEntity<Response> patchTest(@RequestBody CreateTestRequest request, @PathVariable("id") String testIdStr) {
        long testId = Long.parseUnsignedLong(testIdStr);
        Test test = testService.get(testId);
        if (test == null) {
            return ResponseEntity.notFound().build();
        }
        test.setName(request.getName());

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/api/test/{id}")
    public ResponseEntity<Response> deleteTest(@PathVariable("id") String testIdStr) {
        long testId = Long.parseUnsignedLong(testIdStr);
        Test test = testService.get(testId);
        if (test == null) {
            return ResponseEntity.notFound().build();
        }
        testService.remove(test);

        return ResponseEntity.noContent().build();
    }
}
