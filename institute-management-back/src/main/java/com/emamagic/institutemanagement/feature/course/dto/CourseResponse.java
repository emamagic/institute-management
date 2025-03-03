package com.emamagic.institutemanagement.feature.course.dto;

import java.time.LocalDateTime;

public record CourseResponse(
        String id,
        String name,
        LocalDateTime startedAt,
        LocalDateTime endedAt,
        LocalDateTime createdAt
) {
}
