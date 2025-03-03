package com.emamagic.institutemanagementquestion.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import java.util.List;

public record CreateQuestionRequest(
        @NotEmpty(message = "type could not be empty")
        String type,
        @NotEmpty(message = "examId could not be empty")
        String examId,
        @NotEmpty(message = "title could not be empty")
        @Size(max = 10, message = "title could not be more than 10 chars")
        String title,
        @NotEmpty(message = "description could not be empty")
        String description,
        @NotEmpty(message = "score could not be empty")
        String score,
        List<String> options,
        String correctAnswerIndex,
        String descriptiveAnswer
) {
}
