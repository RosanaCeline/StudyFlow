package com.studyflow.backend.service;

import com.studyflow.backend.data.dto.SubjectDTO;
import com.studyflow.backend.data.dto.TaskDTO;
import com.studyflow.backend.exception.UserNotFoundException;
import com.studyflow.backend.model.Subject;
import com.studyflow.backend.model.Task;
import com.studyflow.backend.model.User;
import com.studyflow.backend.repository.SubjectRepository;
import com.studyflow.backend.repository.UserRepository;
import com.studyflow.backend.service.mapper.TaskMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class SubjectService {

    private final SubjectRepository subjectRepository;
    private final UserRepository userRepository;
    private final TaskMapper taskMapper;

    public SubjectDTO create(SubjectDTO dto) {
        User user = getAuthenticatedUser();

        Subject subject = new Subject();
        subject.setName(dto.name());
        subject.setDescription(dto.description());
        subject.setColor(dto.color());
        subject.setCreationDate(
                LocalDateTime.now(ZoneId.of("America/Sao_Paulo"))
        );
        subject.setSituation(dto.situation());
        subject.setUser(user);

        Subject savedSubject = subjectRepository.save(subject);

        return toSubjectDTO(savedSubject);
    }

    public SubjectDTO update(Long id, SubjectDTO dto) {
        User user = getAuthenticatedUser();

        Subject subject = subjectRepository.findByIdAndUserId(id, user.getId())
                .orElseThrow(() -> new RuntimeException("Disciplina não encontrada ou acesso negado"));

        subject.setName(dto.name());
        subject.setDescription(dto.description());
        subject.setColor(dto.color());
        subject.setSituation(dto.situation());

        Subject savedSubject = subjectRepository.save(subject);

        return toSubjectDTO(savedSubject);
    }

    public void delete(Long id) {
        User user = getAuthenticatedUser();

        Subject subject = subjectRepository.findByIdAndUserId(id, user.getId())
                .orElseThrow(() -> new RuntimeException("Disciplina não encontrada ou acesso negado"));

        subjectRepository.delete(subject);
    }

    public List<SubjectDTO> listAll () {
        User user = getAuthenticatedUser();

        List<Subject> subjects = subjectRepository.findAllByUserIdOrderByIdDesc(user.getId());

        return subjects.stream()
                .map(this::toSubjectDTO)
                .toList();
    }

    public SubjectDTO findById(Long id) {
        User user = getAuthenticatedUser();

        Subject subject = subjectRepository.findByIdAndUserId(id, user.getId())
                .orElseThrow(() -> new RuntimeException("Disciplina não encontrada ou acesso negado"));

        return toSubjectDTO(subject);
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

    private User getAuthenticatedUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException());
    }
}
