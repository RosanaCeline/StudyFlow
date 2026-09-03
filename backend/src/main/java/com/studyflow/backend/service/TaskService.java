package com.studyflow.backend.service;

import com.studyflow.backend.data.dto.TaskDTO;
import com.studyflow.backend.model.Task;
import com.studyflow.backend.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TaskService {

    private TaskRepository taskRepository;

    public ResponseEntity<Task> create(TaskDTO dto) {
        Task task = new Task();

        task.setTitle(dto.title());
        task.setDescription(dto.description());
        task.setDeadline(dto.deadline());
        task.setPriority(dto.priority());
        task.setStatus(dto.status());
        task.setCreationDate(dto.creationDate());

        return ResponseEntity.status(HttpStatus.CREATED).body(task);
    }
}
