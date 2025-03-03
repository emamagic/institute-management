package com.emamagic.institutemanagementquestion.service;

import com.emamagic.institutemanagementquestion.dto.*;

import java.util.List;

public interface QuestionService {
    List<QuestionResponse> getAllForBank(Long userId);
    List<QuestionResponse> getAllForExam(Long userId, Long examId);
    void create(Long userId, CreateQuestionRequest req);
    void add(Long userId, AddQuestionRequest req);
    ApprovedQuestionResponse approveQuestions(Long userId, Long examId);
    QuestionResponse update(Long userId, UpdateQuestionRequest req);
    void delete(Long userId, String questionId);
}
