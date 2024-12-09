package ru.danilakondr.testsystem.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.danilakondr.testsystem.data.Participant;
import ru.danilakondr.testsystem.data.Test;
import ru.danilakondr.testsystem.data.User;
import ru.danilakondr.testsystem.protocol.TestBody;
import ru.danilakondr.testsystem.protocol.Description;
import ru.danilakondr.testsystem.protocol.Response;
import ru.danilakondr.testsystem.services.TestService;

import java.lang.reflect.Parameter;
import java.net.URI;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class TestController {
    private final TestService testService;

    @GetMapping("/api/test/{id}")
    public ResponseEntity<Response> getTestInfo(@PathVariable("id") String testIdStr) {
        long testId = Long.parseUnsignedLong(testIdStr);
        Test test = testService.get(testId);

        final Optional<User> currentUser = UserUtils.getCurrentUser();
        if (currentUser.isPresent()) {
            if (!test.isOwnedBy(currentUser.get())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Response.Error.PERMISSION_DENIED);
            }
        }
        else {
            final Optional<Participant> participant = UserUtils.getCurrentParticipant();
            if (participant.isEmpty())
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Response.Error.PERMISSION_DENIED);

            if (testId != participant.get().getTestSession().getTest().getId())
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Response.Error.PERMISSION_DENIED);
        }

        Response.Description response = new Response.Description(new Description.Test(test));
        return ResponseEntity.ok(response);
    }

    @PutMapping("/api/test")
    public ResponseEntity<Response> putTest(@RequestBody TestBody request, @RequestHeader("Host") String host) {
        final Optional<User> testOwner = UserUtils.getCurrentUser();
        if (testOwner.isEmpty())
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Response.Error.PERMISSION_DENIED);

        Test test = testService.create(testOwner.get(), request.getName());
        return ResponseEntity.created(URI.create("http://"+host+"/api/test/"+test.getId())).build();
    }

    @PatchMapping("/api/test/{id}")
    public ResponseEntity<Response> patchTest(@RequestBody TestBody request, @PathVariable("id") String testIdStr) {
        final Optional<User> currentUser = UserUtils.getCurrentUser();
        if (currentUser.isEmpty())
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Response.Error.PERMISSION_DENIED);

        long testId = Long.parseUnsignedLong(testIdStr);
        Test test = testService.get(testId);
        if (!test.isOwnedBy(currentUser.get())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Response.Error.PERMISSION_DENIED);
        }
        test.setName(request.getName());

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/api/test/{id}")
    public ResponseEntity<Response> deleteTest(@PathVariable("id") String testIdStr) {
        final Optional<User> currentUser = UserUtils.getCurrentUser();
        if (currentUser.isEmpty())
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Response.Error.PERMISSION_DENIED);

        long testId = Long.parseUnsignedLong(testIdStr);
        Test test = testService.get(testId);
        if (!test.isOwnedBy(currentUser.get())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Response.Error.PERMISSION_DENIED);
        }
        testService.remove(test);

        return ResponseEntity.noContent().build();
    }
}
