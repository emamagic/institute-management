package com.emamagic.institutemanagementquestion.dto;

import jakarta.validation.constraints.NotEmpty;

public record AddQuestionRequest(
        @NotEmpty(message = "questionId could not be empty")
        String questionId,
        @NotEmpty(message = "examId could not be empty")
        String examId,
        @NotEmpty(message = "score could not be empty")
        String score
) {
}
