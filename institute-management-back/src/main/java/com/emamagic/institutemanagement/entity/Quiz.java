package com.emamagic.institutemanagement.entity;

import com.emamagic.institutemanagement.common.BaseEntity;
import jakarta.persistence.*;

@Entity
@Table(name = "quizzes")
public class Quiz extends BaseEntity {
    @Column(nullable = false)
    private String title;
    @Column(nullable = false)
    private String description;
    @Column(nullable = false)
    private Integer duration;

    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;
}
