package com.emamagic.institutemanagement.entity;

import com.emamagic.institutemanagement.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "courses")
public class Course extends BaseEntity {
    @Column(nullable = false, length = 20)
    private String name;
    @Column(nullable = false)
    private LocalDateTime startedAt;
    @Column(nullable = false)
    private LocalDateTime endedAt;

    @ManyToOne
    @JoinColumn(name = "teacher_id")
    private UserApp teacher;

    // todo: it could be remove
    @ManyToMany
    @JoinTable(
            name = "courses_students",
            joinColumns = @JoinColumn(name = "course_id"),
            inverseJoinColumns = @JoinColumn(name = "student_id")
    )
    List<UserApp> students;


    public void addStudent(UserApp student) {
        students.add(student);
    }

    public void deleteStudent(UserApp student) {
        students.remove(student);
    }
}
