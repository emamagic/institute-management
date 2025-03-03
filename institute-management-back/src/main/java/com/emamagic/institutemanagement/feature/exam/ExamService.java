package com.emamagic.institutemanagement.feature.exam;

import com.emamagic.institutemanagement.common.PageResponse;
import com.emamagic.institutemanagement.feature.exam.dto.ExamRequest;
import com.emamagic.institutemanagement.feature.exam.dto.ExamResponse;

public interface ExamService {
    PageResponse<ExamResponse> exams(int page, int size, Long userId, Long courseId);
    ExamResponse create(Long userId, ExamRequest req);
    ExamResponse update(Long userId, Long examId, ExamRequest req);
    void delete(Long userId, Long examId);

}
