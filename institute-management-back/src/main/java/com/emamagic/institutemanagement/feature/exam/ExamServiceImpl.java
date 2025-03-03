package com.emamagic.institutemanagement.feature.exam;

import com.emamagic.institutemanagement.common.AppMapper;
import com.emamagic.institutemanagement.common.PageResponse;
import com.emamagic.institutemanagement.entity.Course;
import com.emamagic.institutemanagement.entity.Exam;
import com.emamagic.institutemanagement.entity.UserApp;
import com.emamagic.institutemanagement.feature.course.CourseRepository;
import com.emamagic.institutemanagement.feature.exam.dto.ExamRequest;
import com.emamagic.institutemanagement.feature.exam.dto.ExamResponse;
import com.emamagic.institutemanagement.feature.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;
import java.util.Objects;

import static com.emamagic.institutemanagement.common.AppMapper.toResponse;

@Service
@RequiredArgsConstructor
public class ExamServiceImpl implements ExamService {
    private final ExamRepository repo;
    private final CourseRepository courseRepo;
    private final UserRepository userRepo;

    @Override
    public PageResponse<ExamResponse> exams(int page, int size, Long userId, Long courseId) {
        courseRepo.findById(courseId)
                .orElseThrow(() -> new NoSuchElementException("Course not found"));

        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").ascending());

        Exam probe = new Exam();
        probe.setCreator(UserApp.builder().id(userId).build());
        probe.setCourse(Course.builder().id(courseId).build());
        Example<Exam> example = Example.of(probe);

        Page<ExamResponse> examResponse = repo.findAll(example, pageable).map(AppMapper::toResponse);

        return new PageResponse<>(
                examResponse.getContent(),
                examResponse.getNumber(),
                examResponse.getSize(),
                examResponse.getTotalElements(),
                examResponse.getTotalPages(),
                examResponse.isFirst(),
                examResponse.isLast()
        );
    }

    @Override
    public ExamResponse create(Long userId, ExamRequest req) {
        UserApp creator = userRepo.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("User not found"));
        Course course = courseRepo.findById(Long.valueOf(req.courseId()))
                .orElseThrow(() -> new NoSuchElementException("course not found"));

        if (!Objects.equals(userId, course.getTeacher().getId())) {
            throw new RuntimeException("You can only create exam for you courses");
        }

        return toResponse(repo.save(Exam.builder()
                .title(req.title())
                .description(req.description())
                .duration(Integer.valueOf(req.duration()))
                .creator(creator)
                .course(course)
                .build()));
    }

    @Transactional
    @Override
    public ExamResponse update(Long userId, Long examId, ExamRequest req) {
        Exam exam = repo.findById(examId)
                .orElseThrow(() -> new NoSuchElementException("exam not found"));

        if (!exam.getCreator().getId().equals(userId)) {
            throw new RuntimeException("You can only update your exams");
        }

        if (req.title() != null && !req.title().isBlank()) {
            exam.setTitle(req.title());
        }
        if (req.description() != null && !req.description().isBlank()) {
            exam.setDescription(req.description());
        }
        if (req.duration() != null && !req.duration().isBlank()) {
            exam.setDuration(Integer.valueOf(req.duration()));
        }

        return toResponse(exam);
    }

    @Transactional
    @Override
    public void delete(Long userId, Long examId) {
        Exam exam = repo.findById(examId)
                .orElseThrow(() -> new NoSuchElementException("exam not found"));

        if (!Objects.equals(exam.getCreator().getId(), userId)) {
            throw new RuntimeException("You can only delete you exams");
        }

        repo.delete(exam);
    }
}
