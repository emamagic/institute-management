package com.emamagic.institutemanagement.feature.exam.dto;

import jakarta.validation.constraints.NotBlank;

public record ExamRequest(
        @NotBlank(message = "title could not be empty", groups = ExamRequest.Create.class)
        String title,
        @NotBlank(message = "description could not be empty", groups = ExamRequest.Create.class)
        String description,
        @NotBlank(message = "duration could not be empty", groups = ExamRequest.Create.class)
        String duration,
        @NotBlank(message = "course could not be empty", groups = ExamRequest.Create.class)
        String courseId
) {
    public interface Create {
    }
}
