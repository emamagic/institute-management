package com.emamagic.institutemanagement.feature.course.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public /// ISO 8601 format, client has to be used
record CourseRequest(
        @NotBlank(message = "name could not be empty", groups = Create.class)
        String name,
        @NotNull(message = "started_at is not be null or empty", groups = Create.class)
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
        LocalDateTime startedAt,
        @NotNull(message = "ended_at is not be null or empty", groups = Create.class)
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
        LocalDateTime endedAt
) {
    public interface Create {}
}
