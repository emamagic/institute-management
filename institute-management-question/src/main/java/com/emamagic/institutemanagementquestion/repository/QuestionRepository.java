package com.emamagic.institutemanagementquestion.repository;

import com.emamagic.institutemanagementquestion.entity.Question;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionRepository extends MongoRepository<Question, String> {
    List<Question> findByCreatorId(Long creatorId);
    List<Question> findByCreatorIdAndExamId(Long userId, Long examId);
}
