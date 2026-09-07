package com.studyflow.backend.repository;

import com.studyflow.backend.model.Subject;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SubjectRepository extends JpaRepository<Subject, Long> {
    List<Subject> findAllByOrderByIdDesc();
}
