package com.emamagic.institutemanagementquestion.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Document("exam_question")
@CompoundIndexes({
        @CompoundIndex(name = "exam_question_unique", def = "{'examId': 1, 'questionId': 1}", unique = true)
})
public class ExamQuestion {
    @Id
    private String id;
    private String examId;
    private String questionId;
    private Integer score;
}
