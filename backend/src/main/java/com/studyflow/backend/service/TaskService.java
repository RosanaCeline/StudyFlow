package com.studyflow.backend.service;

import com.studyflow.backend.data.dto.TaskDTO;
import com.studyflow.backend.model.Priority;
import com.studyflow.backend.model.Status;
import com.studyflow.backend.model.Subject;
import com.studyflow.backend.model.Task;
import com.studyflow.backend.repository.SubjectRepository;
import com.studyflow.backend.repository.TaskRepository;
import com.studyflow.backend.service.mapper.TaskMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
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
    private final TaskMapper taskMapper;

    public TaskDTO create(TaskDTO dto) {
        Subject subject = subjectRepository.findById(dto.subjectId())
                .orElseThrow(() -> new RuntimeException("Disciplina não encontrada"));

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
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tarefa não encontrada"));

        Subject subject = subjectRepository.findById(dto.subjectId())
                .orElseThrow(() -> new RuntimeException("Disciplina não encontrada"));

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
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tarefa não encontrada"));

        taskRepository.delete(task);

        return;
    }

    public TaskDTO toggleCompleted(Long id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tarefa não encontrada"));

        if(task.getStatus() == Status.COMPLETED) {
            task.setStatus(Status.PENDING);
            task.setCompletionDate(null);
        } else {
            task.setStatus(Status.COMPLETED);
            task.setCompletionDate(
                    LocalDateTime.now(ZoneId.of("America/Sao_Paulo"))
            );
        }

        Task savedTask = taskRepository.save(task);

        return taskMapper.toDTO(savedTask);
    }

    public List<TaskDTO> filter(Long subjectId, Status status, Priority priority) {

        List<Specification<Task>> specifications = new ArrayList<>();

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
}
