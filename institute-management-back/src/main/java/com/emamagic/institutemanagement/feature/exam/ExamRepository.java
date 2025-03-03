package com.emamagic.institutemanagement.feature.exam;

import com.emamagic.institutemanagement.entity.Exam;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExamRepository extends JpaRepository<Exam, Long> {
}
