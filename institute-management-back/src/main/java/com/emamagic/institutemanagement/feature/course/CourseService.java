package com.emamagic.institutemanagement.feature.course;

import com.emamagic.institutemanagement.common.PageResponse;
import com.emamagic.institutemanagement.feature.course.dto.CourseRequest;
import com.emamagic.institutemanagement.feature.course.dto.CourseResponse;
import com.emamagic.institutemanagement.feature.user.dto.UserResponse;

import java.util.List;

public interface CourseService {
    PageResponse<CourseResponse> findAll(int page, int size);
    CourseResponse create(CourseRequest req);
    void enrollUser(Long courseId, Long userId);
    CourseResponse update(Long id, CourseRequest req);
    void remove(Long id);
    void withdrawalUser(Long courseId, Long userId);
    List<UserResponse> usersByCourseId(Long courseId);
    CourseResponse findById(Long courseId);
}
