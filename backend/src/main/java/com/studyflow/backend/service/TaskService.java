package com.studyflow.backend.service;

import com.studyflow.backend.data.dto.TaskDTO;
import com.studyflow.backend.exception.UserNotFoundException;
import com.studyflow.backend.model.*;
import com.studyflow.backend.repository.SubjectRepository;
import com.studyflow.backend.repository.TaskRepository;
import com.studyflow.backend.repository.UserRepository;
import com.studyflow.backend.service.mapper.TaskMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class TaskService {

    private final SubjectRepository subjectRepository;
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final TaskMapper taskMapper;

    public TaskDTO create(TaskDTO dto) {
        User user = getAuthenticatedUser();

        Subject subject = subjectRepository.findByIdAndUserId(dto.subjectId(), user.getId())
                .orElseThrow(() -> new RuntimeException("Disciplina não encontrada ou acesso negado"));

        Task task = new Task();

        task.setTitle(dto.title());
        task.setDescription(dto.description());
        task.setDeadline(dto.deadline());
        task.setPriority(dto.priority());
        task.setStatus(dto.status());
        task.setCreationDate(
                LocalDateTime.now(ZoneId.of("America/Sao_Paulo"))
        );
        task.setSubject(subject);

        Task savedTask = taskRepository.save(task);

        return taskMapper.toDTO(savedTask);
    }

    public TaskDTO update(Long id, TaskDTO dto) {
        User user = getAuthenticatedUser();

        Task task = taskRepository.findByIdAndSubjectUserId(id, user.getId())
                .orElseThrow(() -> new RuntimeException("Tarefa não encontrada ou acesso negado"));

        Subject subject = subjectRepository.findByIdAndUserId(dto.subjectId(), user.getId())
                .orElseThrow(() -> new RuntimeException("Disciplina não encontrada ou acesso negado"));

        task.setTitle(dto.title());
        task.setDescription(dto.description());
        task.setDeadline(dto.deadline());
        task.setPriority(dto.priority());
        task.setStatus(dto.status());
        task.setSubject(subject);

        if (dto.status() == Status.COMPLETED) {
            if (task.getCompletionDate() == null) {
                task.setCompletionDate(
                        LocalDateTime.now(ZoneId.of("America/Sao_Paulo"))
                );
            }
        } else {
            task.setCompletionDate(null);
        }

        Task savedTask = taskRepository.save(task);

        return taskMapper.toDTO(savedTask);
    }

    public void delete(Long id) {
        User user = getAuthenticatedUser();

        Task task = taskRepository.findByIdAndSubjectUserId(id, user.getId())
                .orElseThrow(() -> new RuntimeException("Tarefa não encontrada ou acesso negado"));

        taskRepository.delete(task);

        return;
    }

    public TaskDTO toggleStatus(Long id, Status status) {
        User user = getAuthenticatedUser();

        Task task = taskRepository.findByIdAndSubjectUserId(id, user.getId())
                .orElseThrow(() -> new RuntimeException("Tarefa não encontrada ou acesso negado"));

        task.setStatus(status);

        if(task.getStatus() == Status.COMPLETED) {
            task.setCompletionDate(
                    LocalDateTime.now(ZoneId.of("America/Sao_Paulo"))
            );
        } else {
            task.setCompletionDate(null);
        }

        Task savedTask = taskRepository.save(task);

        return taskMapper.toDTO(savedTask);
    }

    public List<TaskDTO> filter(Long subjectId, Status status, Priority priority) {
        User user = getAuthenticatedUser();

        List<Specification<Task>> specifications = new ArrayList<>();

        specifications.add((root, query, cb) ->
                cb.equal(root.get("subject").get("user").get("id"), user.getId())
        );

        if (subjectId != null) {
            specifications.add((root, query, cb) ->
                    cb.equal(root.get("subject").get("id"), subjectId)
            );
        }

        if (status != null) {
            specifications.add((root, query, cb) ->
                    cb.equal(root.get("status"), status)
            );
        }

        if (priority != null) {
            specifications.add((root, query, cb) ->
                    cb.equal(root.get("priority"), priority)
            );
        }

        Specification<Task> specification = Specification.allOf(specifications);

        List<Task> tasks = taskRepository.findAll(specification);

        return tasks.stream()
                .map(taskMapper::toDTO)
                .toList();
    }

    private User getAuthenticatedUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException());
    }
}
