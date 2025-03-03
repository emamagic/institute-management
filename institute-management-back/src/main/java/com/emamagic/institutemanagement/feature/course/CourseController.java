package com.emamagic.institutemanagement.feature.course;

import com.emamagic.institutemanagement.common.PageResponse;
import com.emamagic.institutemanagement.feature.course.dto.CourseRequest;
import com.emamagic.institutemanagement.feature.course.dto.CourseResponse;
import com.emamagic.institutemanagement.feature.user.dto.UserResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//todo: move all authorization in SecurityFilterChain, if it possible
@RestController
@RequestMapping("/courses")
@RequiredArgsConstructor
@Tag(name = "Course")
public class CourseController {
    private final CourseService svc;

    @GetMapping
    public ResponseEntity<PageResponse<CourseResponse>> courses(
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(svc.findAll((page - 1), size));
    }

    @GetMapping("/{course-id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<CourseResponse> course(@PathVariable("course-id") String courseId) {
        return ResponseEntity.ok(svc.findById(Long.valueOf(courseId)));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<CourseResponse> create(@RequestBody @Validated(CourseRequest.Create.class) CourseRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(svc.create(req));
    }

    @PostMapping("/{course-id}/users/{user-id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Void> enrollUser(
            @PathVariable("course-id") String courseId,
            @PathVariable("user-id") String userId
    ) {
        svc.enrollUser(Long.valueOf(courseId), Long.valueOf(userId));
        return ResponseEntity.ok().build();
    }


    @PatchMapping("/{course-id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<CourseResponse> update(
            @PathVariable("course-id") String id,
            @RequestBody @Valid CourseRequest req
    ) {
        return ResponseEntity.ok(svc.update(Long.valueOf(id), req));
    }

    @DeleteMapping("/{course-id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable("course-id") String id) {
        svc.remove(Long.valueOf(id));
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{course-id}/users/{user-id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Void> withdrawalUser(
            @PathVariable("course-id") String courseId,
            @PathVariable("user-id") String userId
    ) {
        svc.withdrawalUser(Long.valueOf(courseId), Long.valueOf(userId));
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{course-id}/users")
    public ResponseEntity<List<UserResponse>> usersByCourseId(
            @PathVariable("course-id") String courseId
    ) {
        return ResponseEntity.ok(svc.usersByCourseId(Long.valueOf(courseId)));
    }

}


