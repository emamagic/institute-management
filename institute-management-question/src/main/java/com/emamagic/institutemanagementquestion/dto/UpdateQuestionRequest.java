package com.emamagic.institutemanagementquestion.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public record UpdateQuestionRequest(
        @NotEmpty(message = "title could not be empty")
        @Size(max = 10, message = "title could not be more than 10 chars")
        String title,
        @NotEmpty(message = "description could not be empty")
        String description,
        @NotEmpty(message = "score could not be empty")
        String score
) {
}
