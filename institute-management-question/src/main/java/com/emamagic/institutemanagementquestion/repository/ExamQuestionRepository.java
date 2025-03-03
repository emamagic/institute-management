package com.emamagic.institutemanagementquestion.repository;

import com.emamagic.institutemanagementquestion.entity.ExamQuestion;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExamQuestionRepository extends MongoRepository<ExamQuestion, String> {
    List<ExamQuestion> findByExamId(String examId);

    ExamQuestion findByExamIdAndQuestionId(Long examId, String id);
}
