package com.emamagic.institutemanagement.common;

import com.emamagic.institutemanagement.entity.Course;
import com.emamagic.institutemanagement.entity.Exam;
import com.emamagic.institutemanagement.entity.UserApp;
import com.emamagic.institutemanagement.feature.course.dto.CourseResponse;
import com.emamagic.institutemanagement.feature.exam.dto.ExamResponse;
import com.emamagic.institutemanagement.feature.user.Gender;
import com.emamagic.institutemanagement.feature.user.dto.UserResponse;
import lombok.experimental.UtilityClass;

@UtilityClass
public class AppMapper {

    public static UserResponse toResponse(UserApp user) {
        Gender gender = user.getGender();
        return new UserResponse(
                String.valueOf(user.getId()),
                user.getName(),
                user.getEmail(),
                user.getAge(),
                (gender == null) ? null : gender.name(),
                user.getRole().getName(),
                user.getIsVerified(),
                user.getIsApproved(),
                user.getCreatedAt()
        );
    }

    public static CourseResponse toResponse(Course course) {
        return new CourseResponse(
                String.valueOf(course.getId()),
                course.getName(),
                course.getStartedAt(),
                course.getEndedAt(),
                course.getCreatedAt()
        );
    }

    public static ExamResponse toResponse(Exam exam) {
        return new ExamResponse(
                String.valueOf(exam.getId()),
                exam.getTitle(),
                exam.getDescription(),
                String.valueOf(exam.getDuration()),
                String.valueOf(exam.getCreator().getId()),
                String.valueOf(exam.getCourse().getId())
        );
    }
}
