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
}
