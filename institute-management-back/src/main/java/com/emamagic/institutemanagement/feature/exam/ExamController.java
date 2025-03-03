package com.emamagic.institutemanagement.feature.exam;

import com.emamagic.institutemanagement.common.PageResponse;
import com.emamagic.institutemanagement.feature.exam.dto.ExamRequest;
import com.emamagic.institutemanagement.feature.exam.dto.ExamResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/exams")
@RequiredArgsConstructor
@Tag(name = "Exam")
public class ExamController {
    private final ExamService svc;

    @GetMapping
    @PreAuthorize("hasAuthority('TEACHER')")
    public ResponseEntity<PageResponse<ExamResponse>> exams(
            @AuthenticationPrincipal(expression = "id") Long id,
            @RequestParam("course-id") String courseId,
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(svc.exams((page - 1), size, id, Long.valueOf(courseId)));
    }

    @PutMapping
    @PreAuthorize("hasAuthority('TEACHER')")
    public ResponseEntity<ExamResponse> create(
            @AuthenticationPrincipal(expression = "id") Long id,
            @RequestBody @Validated(ExamRequest.Create.class) ExamRequest req
    ) {
        return ResponseEntity.ok(svc.create(id, req));
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasAuthority('TEACHER')")
    public ResponseEntity<ExamResponse> update(
            @AuthenticationPrincipal(expression = "id") Long id,
            @PathVariable("id") String examId,
            @RequestBody ExamRequest req
    ) {
        return ResponseEntity.ok(svc.update(id, Long.valueOf(examId), req));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('TEACHER')")
    public ResponseEntity<Void> delete(
            @AuthenticationPrincipal(expression = "id") Long id,
            @PathVariable("id") String examId
    ) {
        svc.delete(id, Long.valueOf(examId));
        return ResponseEntity.ok().build();
    }
}
