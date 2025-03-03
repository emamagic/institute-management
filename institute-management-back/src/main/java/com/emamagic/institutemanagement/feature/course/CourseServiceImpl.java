package com.emamagic.institutemanagement.feature.course;

import com.emamagic.institutemanagement.common.PageResponse;
import com.emamagic.institutemanagement.entity.Course;
import com.emamagic.institutemanagement.entity.UserApp;
import com.emamagic.institutemanagement.common.AppMapper;
import com.emamagic.institutemanagement.feature.course.dto.CourseRequest;
import com.emamagic.institutemanagement.feature.course.dto.CourseResponse;
import com.emamagic.institutemanagement.feature.user.UserRepository;
import com.emamagic.institutemanagement.feature.user.dto.UserResponse;
import com.emamagic.institutemanagement.feature.user.role.RoleType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class CourseServiceImpl implements CourseService {
    private final CourseRepository repo;
    private final UserRepository userRepo;

    @Override
    public PageResponse<CourseResponse> findAll(int page, int size) {
        UserApp user = (UserApp) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").ascending());
        Page<Course> courses = null;
        switch (user.getRole().getType()) {
            case RoleType.ADMIN -> courses = repo.findAll(pageable);
            case RoleType.TEACHER -> courses = repo.findAllByTeacherId(user.getId(), pageable);
            case RoleType.STUDENT -> throw new RuntimeException("not implemented yet");
        }
        List<CourseResponse> courseResponses = courses.stream().map(AppMapper::toResponse).toList();

        return new PageResponse<>(
                courseResponses,
                courses.getNumber(),
                courses.getSize(),
                courses.getTotalElements(),
                courses.getTotalPages(),
                courses.isFirst(),
                courses.isLast()
        );
    }

    @Override
    public CourseResponse create(CourseRequest req) {
        Optional<Course> existedCourse = repo.findByName(req.name());
        if (existedCourse.isPresent()) {
            throw new RuntimeException("There is a Course with this name");
        }
        validateStartEndDates(req.startedAt(), req.endedAt());
        Course course = Course.builder()
                .name(req.name())
                .startedAt(req.startedAt())
                .endedAt(req.endedAt())
                .build();
        course = repo.save(course);

        return new CourseResponse(
                String.valueOf(course.getId()),
                course.getName(),
                course.getStartedAt(),
                course.getEndedAt(),
                course.getCreatedAt()
        );
    }

    @Transactional
    @Override
    public void enrollUser(Long courseId, Long userId) {
        UserApp user = userRepo.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("user not found"));

        Course course = repo.findById(courseId)
                .orElseThrow(() -> new NoSuchElementException("course not found"));

        switch (user.getRole().getType()) {
            case RoleType.ADMIN -> throw new IllegalArgumentException("admin can not enroll for a course");
            case RoleType.TEACHER -> course.setTeacher(user);
            case RoleType.STUDENT -> course.addStudent(user);
        }

    }

    @Transactional
    @Override
    public CourseResponse update(Long id, CourseRequest req) {
        Course course = repo.findById(id)
                .orElseThrow(() -> new NoSuchElementException("course not found"));

        if (req.name() != null && !req.name().isBlank()) course.setName(req.name());
        if (req.startedAt() != null && req.endedAt() != null) {
            validateStartEndDates(req.startedAt(), req.endedAt());
            course.setStartedAt(req.startedAt());
            course.setEndedAt(req.endedAt());
        }
        if (req.startedAt() != null && req.endedAt() == null) {
            validateStartEndDates(req.startedAt(), course.getEndedAt());
            course.setStartedAt(req.startedAt());
        }
        if (req.startedAt() == null && req.endedAt() != null) {
            validateStartEndDates(course.getStartedAt(), course.getEndedAt());
            course.setEndedAt(req.endedAt());
        }
        return AppMapper.toResponse(course);
    }

    @Transactional
    @Override
    public void remove(Long id) {
        repo.findById(id)
                .orElseThrow(() -> new NoSuchElementException("course not found"));
        repo.deleteById(id);
    }

    @Transactional
    @Override
    public void withdrawalUser(Long courseId, Long userId) {
        UserApp user = userRepo.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("user not found"));

        Course course = repo.findById(courseId)
                .orElseThrow(() -> new NoSuchElementException("course not found"));

        switch (user.getRole().getType()) {
            case RoleType.TEACHER -> course.setTeacher(null);
            case RoleType.STUDENT -> course.deleteStudent(user);
        }
    }

    @Override
    public List<UserResponse> usersByCourseId(Long courseId) {
        Course course = repo.findById(courseId)
                .orElseThrow(() -> new NoSuchElementException("course not found"));

        List<UserApp> users = new ArrayList<>();
        users.add(course.getTeacher());
        users.addAll(course.getStudents());

        return users.stream()
                .filter(Objects::nonNull)
                .map(AppMapper::toResponse)
                .toList();
    }

    @Override
    public CourseResponse findById(Long courseId) {
        return repo.findById(courseId).map(AppMapper::toResponse)
                .orElseThrow(() -> new NoSuchElementException("there is no such course"));
    }

    public static void validateStartEndDates(LocalDateTime startedAt, LocalDateTime endedAt) {
        if (startedAt.isAfter(endedAt)) {
            throw new IllegalArgumentException("ended-at must be after started-at");
        }
    }
}
