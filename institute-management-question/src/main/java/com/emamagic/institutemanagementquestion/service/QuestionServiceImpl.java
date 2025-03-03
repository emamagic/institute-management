package com.emamagic.institutemanagementquestion.service;

import com.emamagic.institutemanagementquestion.dto.*;
import com.emamagic.institutemanagementquestion.entity.ExamQuestion;
import com.emamagic.institutemanagementquestion.entity.Question;
import com.emamagic.institutemanagementquestion.entity.Type;
import com.emamagic.institutemanagementquestion.repository.ExamQuestionRepository;
import com.emamagic.institutemanagementquestion.repository.QuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class QuestionServiceImpl implements QuestionService {
    private final QuestionRepository repo;
    private final ExamQuestionRepository scoreRepo;

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

    }

    @Override
    public ApprovedQuestionResponse approveQuestions(Long userId, Long examId) {
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
        Integer score = 0;
        if (examId == null) {
//            score = scoreRepo
        } else {
            score = scoreRepo.findByExamIdAndQuestionId(examId, q.getId()).getScore();
        }

        return new QuestionResponse(
                q.getId(),
                q.getType().name(),
                q.getTitle(),
                String.valueOf(score),
                q.getIsApproved(),
                q.getBody()
        );
    }

    private Map<String, Object> generateBody(CreateQuestionRequest req) {

    }
}
