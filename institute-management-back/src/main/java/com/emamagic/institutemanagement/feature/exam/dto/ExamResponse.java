package com.emamagic.institutemanagement.feature.exam.dto;

public record ExamResponse(
        String id,
        String title,
        String description,
        String duration,
        String creatorId,
        String courseId
) {
}
