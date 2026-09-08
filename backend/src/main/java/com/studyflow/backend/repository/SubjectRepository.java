package com.studyflow.backend.repository;

import com.studyflow.backend.model.Subject;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SubjectRepository extends JpaRepository<Subject, Long> {

    List<Subject> findAllByUserIdOrderByIdDesc(Long userId);

    Optional<Subject> findByIdAndUserId(Long id, Long userId);

    List<Subject> findAllByOrderByIdDesc();
}
