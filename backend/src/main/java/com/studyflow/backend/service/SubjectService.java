package com.studyflow.backend.service;

import com.studyflow.backend.data.dto.SubjectDTO;
import com.studyflow.backend.data.dto.TaskDTO;
import com.studyflow.backend.model.Subject;
import com.studyflow.backend.model.Task;
import com.studyflow.backend.repository.SubjectRepository;
import com.studyflow.backend.service.mapper.TaskMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class SubjectService {

    private final SubjectRepository subjectRepository;
    private final TaskMapper taskMapper;

    public SubjectDTO create(SubjectDTO dto) {
        Subject subject = new Subject();

        subject.setName(dto.name());
        subject.setDescription(dto.description());
        subject.setColor(dto.color());
        subject.setCreationDate(
                LocalDateTime.now(ZoneId.of("America/Sao_Paulo"))
        );
        subject.setSituation(dto.situation());

        Subject savedSubject = subjectRepository.save(subject);

        return toSubjectDTO(savedSubject);
    }

    public SubjectDTO update(Long id, SubjectDTO dto) {
        Subject subject = subjectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Disciplina não encontrada"));

        subject.setName(dto.name());
        subject.setDescription(dto.description());
        subject.setColor(dto.color());
        subject.setSituation(dto.situation());

        Subject savedSubject = subjectRepository.save(subject);

        return toSubjectDTO(savedSubject);
    }

    public void delete(Long id) {
        Subject subject = subjectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Disciplina não encontrada"));

        subjectRepository.delete(subject);

        return;
    }

    public List<SubjectDTO> listAll () {
        List<Subject> subjects = subjectRepository.findAll();

        return subjects.stream()
                .map(this::toSubjectDTO)
                .toList();
    }

    private SubjectDTO toSubjectDTO(Subject subject) {
        return new SubjectDTO(
                subject.getId(),
                subject.getName(),
                subject.getDescription(),
                subject.getColor(),
                subject.getSituation(),
                subject.getTask().stream().map(taskMapper::toDTO).toList(),
                subject.getCreationDate()
        );
    }
}
