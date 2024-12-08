package ru.danilakondr.testsystem.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.danilakondr.testsystem.data.Test;
import ru.danilakondr.testsystem.data.User;
import ru.danilakondr.testsystem.protocol.TestBody;
import ru.danilakondr.testsystem.protocol.Description;
import ru.danilakondr.testsystem.protocol.Response;
import ru.danilakondr.testsystem.services.TestService;

import java.net.URI;

@Controller
@RequiredArgsConstructor
public class TestController {
    private final TestService testService;

    // TODO implement get test info for participants
    @GetMapping("/api/test/{id}")
    public ResponseEntity<Response> getTestInfo(@PathVariable("id") String testIdStr) {
        final User currentUser = UserUtils.getCurrentUser();

        long testId = Long.parseUnsignedLong(testIdStr);
        Test test = testService.get(testId);
        if (test == null) {
            return ResponseEntity.notFound().build();
        }
        if (!test.isOwnedBy(currentUser)) {
            return ResponseEntity.status(403).body(new Response.Error("PERMISSION_DENIED"));
        }

        Response.Description response = new Response.Description(new Description.Test(test));
        return ResponseEntity.ok(response);
    }

    @PutMapping("/api/test")
    public ResponseEntity<Response> putTest(@RequestBody TestBody request, @RequestHeader("Host") String host) {
        final User testOwner = UserUtils.getCurrentUser();

        Test test = testService.create(testOwner, request.getName());
        return ResponseEntity.created(URI.create("http://"+host+"/api/test/"+test.getId())).build();
    }

    @PatchMapping("/api/test/{id}")
    public ResponseEntity<Response> patchTest(@RequestBody TestBody request, @PathVariable("id") String testIdStr) {
        final User currentUser = UserUtils.getCurrentUser();

        long testId = Long.parseUnsignedLong(testIdStr);
        Test test = testService.get(testId);
        if (test == null) {
            return ResponseEntity.notFound().build();
        }
        if (!test.isOwnedBy(currentUser)) {
            return ResponseEntity.status(403).body(new Response.Error("PERMISSION_DENIED"));
        }
        test.setName(request.getName());

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/api/test/{id}")
    public ResponseEntity<Response> deleteTest(@PathVariable("id") String testIdStr) {
        final User currentUser = UserUtils.getCurrentUser();

        long testId = Long.parseUnsignedLong(testIdStr);
        Test test = testService.get(testId);
        if (test == null) {
            return ResponseEntity.notFound().build();
        }
        if (!test.isOwnedBy(currentUser)) {
            return ResponseEntity.status(403).body(new Response.Error("PERMISSION_DENIED"));
        }
        testService.remove(test);

        return ResponseEntity.noContent().build();
    }
}
