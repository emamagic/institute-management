package com.emamagic.institutemanagementquestion.service;

import com.emamagic.institutemanagementquestion.dto.*;
import com.emamagic.institutemanagementquestion.entity.ExamQuestion;
import com.emamagic.institutemanagementquestion.entity.Question;
import com.emamagic.institutemanagementquestion.entity.Type;
import com.emamagic.institutemanagementquestion.repository.ExamQuestionRepository;
import com.emamagic.institutemanagementquestion.repository.QuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class QuestionServiceImpl implements QuestionService {
    private final QuestionRepository repo;
    private final ExamQuestionRepository scoreRepo;
    ///  for bulk update
    private final MongoTemplate mongoTemplate;

    @Override
    public List<QuestionResponse> getAllForBank(Long userId) {
        return repo.findByCreatorId(userId).stream()
                .map(q -> mapTo(q, null))
                .toList();
    }

    @Override
    public List<QuestionResponse> getAllForExam(Long userId, Long examId) {
        return repo.findByCreatorIdAndExamId(userId, examId).stream()
                .map(q -> mapTo(q, examId))
                .toList();
    }

    @Transactional
    @Override
    public void create(Long userId, CreateQuestionRequest req) {
        Question question = Question.builder()
                .isApproved(false)
                .type(Type.valueOf(req.type()))
                .creatorId(userId)
                .title(req.title())
                .description(req.description())
                .body(generateBody(req))
                .build();
        question.addExamId(req.examId());

        Question savedQuestion = repo.save(question);

        ExamQuestion eq = ExamQuestion.builder()
                .id(req.examId() + savedQuestion.getId())
                .examId(req.examId())
                .questionId(savedQuestion.getId())
                .score(Integer.valueOf(req.score()))
                .build();

        scoreRepo.save(eq);
    }

    @Transactional
    @Override
    public void add(Long userId, AddQuestionRequest req) {
        Question currentQuestion = repo.findById(req.questionId())
                .orElseThrow(() -> new NoSuchElementException("question not found"));

        currentQuestion.addExamId(req.examId());

        scoreRepo.save(ExamQuestion.builder()
                .questionId(req.questionId())
                .examId(req.examId())
                .score(Integer.valueOf(req.score()))
                .build());
    }

    @Override
    public ApprovedQuestionResponse approveQuestion(Long userId, Long examId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("creator_id").is(userId)
                .and("exam_id").in(examId));
        Update update = new Update();
        update.set("isApproved", true);

        var result = mongoTemplate.updateMulti(query, update, Question.class);

        ApprovedQuestionResponse response = new ApprovedQuestionResponse();
        response.setApprovedCount(result.getModifiedCount());

        return null;
    }

    @Transactional
    @Override
    public QuestionResponse update(Long userId, UpdateQuestionRequest req) {
        return null;
    }

    @Transactional
    @Override
    public void delete(Long userId, String questionId) {

    }

    private QuestionResponse mapTo(Question q, Long examId) {
        //todo: find all questionExam and assign them to the corresponded question in memory
        String score = (examId != null) ? String.valueOf(scoreRepo.findByExamIdAndQuestionId(examId, q.getId()).getScore()) : null;

        return new QuestionResponse(
                q.getId(),
                q.getType().name(),
                q.getTitle(),
                score,
                q.getIsApproved(),
                q.getBody()
        );
    }

    private Map<String, Object> generateBody(CreateQuestionRequest req) {
        return null;
    }
}
