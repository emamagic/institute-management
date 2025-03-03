package com.emamagic.institutemanagementquestion.controller;

import com.emamagic.institutemanagementquestion.dto.QuestionResponse;
import com.emamagic.institutemanagementquestion.service.QuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/questions")
@RequiredArgsConstructor
public class QuestionController {
    private final QuestionService svc;

    //todo: add global exception
    //todo: add config-server and eureka
    //todo: security configuration (gateway-jwt)

    @GetMapping
    @PreAuthorize("hasAuthority('TEACHER')")
    public ResponseEntity<List<QuestionResponse>> questions(
            @AuthenticationPrincipal(expression = "id") Long userId,
            @RequestParam("course-id") String courseId
    ) {
//        return ResponseEntity.ok(svc.getAll(userId, Long.valueOf(courseId)));
        return null;
    }

    @PostMapping("/{id}")
    @PreAuthorize("hasAuthority('TEACHER')")
    public ResponseEntity<Void> add(
            @AuthenticationPrincipal(expression = "id") Long userId,
            @PathVariable("id") String questionId
    ) {
        return ResponseEntity.ok().build();
    }


}

