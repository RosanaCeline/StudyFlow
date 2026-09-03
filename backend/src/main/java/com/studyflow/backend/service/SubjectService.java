package com.studyflow.backend.service;

import com.studyflow.backend.data.dto.SubjectDTO;
import com.studyflow.backend.model.Subject;
import com.studyflow.backend.repository.SubjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SubjectService {

    private SubjectRepository subjectRepository;

    public ResponseEntity<Subject> create(SubjectDTO dto) {
        Subject subject = new Subject();

        subject.setName(dto.name());
        subject.setDescription(dto.description());
        subject.setColor(dto.color());
        subject.setCreationDate(
                LocalDateTime.now(ZoneId.of("America/Sao_Paulo"))
        );
        subject.setSituation(dto.situation());

        subjectRepository.save(subject);

        return ResponseEntity.status(HttpStatus.CREATED).body(subject);
    }

    public ResponseEntity<Subject> update(Long id, SubjectDTO dto) {
        Subject subject = subjectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Disciplina não encontrada"));

        subject.setName(dto.name());
        subject.setDescription(dto.description());
        subject.setColor(dto.color());
        subject.setSituation(dto.situation());

        subjectRepository.save(subject);

        return ResponseEntity.ok(subject);
    }

    public ResponseEntity<Void> delete(Long id) {
        Subject subject = subjectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Disciplina não encontrada"));

        subjectRepository.delete(subject);

        return ResponseEntity.noContent().build();
    }

    public List<Subject> listAll () {
        List<Subject> subjects = subjectRepository.findAll();

        return subjects;
    }
}
