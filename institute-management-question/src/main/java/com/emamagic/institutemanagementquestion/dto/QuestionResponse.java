package com.emamagic.institutemanagementquestion.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Map;


@JsonInclude(JsonInclude.Include.NON_EMPTY)
public record QuestionResponse(
        String id,
        String type,
        String title,
        String score,
        Boolean isApproved,
        Map<String, Object> body
) {
}
