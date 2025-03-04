package com.emamagic.institutemanagementquestion.controller;

import com.emamagic.institutemanagementquestion.dto.ApprovedQuestionResponse;
import com.emamagic.institutemanagementquestion.dto.CreateQuestionRequest;
import com.emamagic.institutemanagementquestion.dto.QuestionResponse;
import com.emamagic.institutemanagementquestion.dto.UpdateQuestionRequest;
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

    // todo: check authorities in security-filter-chain instead of here

    @GetMapping
    @PreAuthorize("hasAuthority('TEACHER')")
    public ResponseEntity<List<QuestionResponse>> questionsForBank(
            @AuthenticationPrincipal(expression = "id") Long userId
    ) {
        return ResponseEntity.ok(svc.getAllForBank(userId));
    }

    @GetMapping
    @PreAuthorize("hasAuthority('TEACHER')")
    public ResponseEntity<List<QuestionResponse>> questionsForExam(
            @AuthenticationPrincipal(expression = "id") Long userId,
            @RequestParam("exam-id") String examId
    ) {
        return ResponseEntity.ok(svc.getAllForExam(userId, Long.valueOf(examId)));
    }


    @PostMapping
    @PreAuthorize("hasAuthority('TEACHER')")
    public ResponseEntity<Void> create(
            @AuthenticationPrincipal(expression = "id") Long userId,
            @RequestBody CreateQuestionRequest req
    ) {
        svc.create(userId, req);
        return ResponseEntity.ok().build();
    }

    @PatchMapping
    @PreAuthorize("hasAuthority('TEACHER')")
    public ResponseEntity<ApprovedQuestionResponse> approveQuestion(
            @AuthenticationPrincipal(expression = "id") Long userId,
            @RequestParam("exam-id") String examId
    ) {
        return ResponseEntity.ok(svc.approveQuestion(userId, Long.valueOf(examId)));
    }

    @PostMapping("/{id}")
    @PreAuthorize("hasAuthority('TEACHER')")
    public ResponseEntity<Void> add(
            @AuthenticationPrincipal(expression = "id") Long userId,
            @PathVariable("id") String questionId
    ) {
        return ResponseEntity.ok().build();
    }

    @PutMapping("")
    @PreAuthorize("hasAuthority('TEACHER')")
    public ResponseEntity<QuestionResponse> update(
            @AuthenticationPrincipal(expression = "id") Long userId,
            @RequestBody UpdateQuestionRequest req
    ) {
        return ResponseEntity.ok(svc.update(userId, req));
    }

    @DeleteMapping
    @PreAuthorize("hasAuthority('TEACHER')")
    public ResponseEntity<Void> delete(
            @AuthenticationPrincipal(expression = "id") Long userId,
            @RequestParam("question-id") String questionId
    ) {
        svc.delete(userId, questionId);
        return ResponseEntity.ok().build();
    }

}

