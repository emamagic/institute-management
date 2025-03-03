package com.emamagic.institutemanagementquestion.entity;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Builder
@NotEmpty
@AllArgsConstructor
@Data
@Document("questions")
public class Question {
    @Id
    private String id;
    private Boolean isApproved;
    private Type type;
    @Field("creator_id")
    private Long creatorId;
    @Field("exam_id")
    private List<Long> examIds;
    @Indexed
    private String title;
    private String description;
    @Field("body")
    private Map<String, Object> body;

    public void addExamId(Long examId) {
        //todo: could it be optimized??
        examIds = new ArrayList<>(examIds);
        examIds.add(examId);
    }

    public void addExamId(String examId) {
        examIds = new ArrayList<>(examIds);
        Long id = Long.valueOf(examId);
        examIds.add(id);
    }
}
