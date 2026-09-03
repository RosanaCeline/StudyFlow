package com.studyflow.backend.service;

import com.studyflow.backend.data.dto.TaskDTO;
import com.studyflow.backend.model.Status;
import com.studyflow.backend.model.Subject;
import com.studyflow.backend.model.Task;
import com.studyflow.backend.repository.SubjectRepository;
import com.studyflow.backend.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;

@Service
@RequiredArgsConstructor
public class TaskService {

    private SubjectRepository subjectRepository;
    private TaskRepository taskRepository;

    public ResponseEntity<Task> create(TaskDTO dto) {
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

        return ResponseEntity.status(HttpStatus.CREATED).body(task);
    }

    public ResponseEntity<Task> update(Long id, TaskDTO dto) {
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

        Task updatedTask = taskRepository.save(task);

        return ResponseEntity.ok(updatedTask);
    }

    public ResponseEntity<Void> delete(Long id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tarefa não encontrada"));

        taskRepository.delete(task);

        return ResponseEntity.noContent().build();
    }

    public ResponseEntity<Task> toggleCompleted(Long id) {
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

        taskRepository.save(task);

        return ResponseEntity.ok(task);
    }
}
