package com.studyflow.backend.controller;

import com.studyflow.backend.controller.docs.TaskControllerDocs;
import com.studyflow.backend.data.dto.TaskDTO;
import com.studyflow.backend.model.Priority;
import com.studyflow.backend.model.Status;
import com.studyflow.backend.model.Task;
import com.studyflow.backend.service.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tasks")
@RequiredArgsConstructor
public class TaskController implements TaskControllerDocs {

    private final TaskService taskService;

    @PostMapping
    public ResponseEntity<TaskDTO> create(@RequestBody @Valid TaskDTO dto) {
        TaskDTO task = taskService.create(dto);

        return ResponseEntity.status(HttpStatus.CREATED).body(task);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TaskDTO> update(@PathVariable Long id, @RequestBody @Valid TaskDTO dto) {
        TaskDTO updatedTask = taskService.update(id, dto);
        return ResponseEntity.ok(updatedTask);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        taskService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/toggle")
    public ResponseEntity<TaskDTO> toggleCompleted(@PathVariable Long id) {
        TaskDTO task = taskService.toggleCompleted(id);
        return ResponseEntity.ok(task);
    }

    @GetMapping
    public ResponseEntity<List<TaskDTO>> filter(
            @RequestParam(required = false) Long subjectId,
            @RequestParam(required = false) Status status,
            @RequestParam(required = false) Priority priority) {

        List<TaskDTO> tasks = taskService.filter(subjectId, status, priority);
        return ResponseEntity.ok(tasks);
    }
}
