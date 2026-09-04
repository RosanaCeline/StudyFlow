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
    public ResponseEntity<Task> create(@RequestBody @Valid TaskDTO dto) {
        Task task = taskService.create(dto);

        return ResponseEntity.status(HttpStatus.CREATED).body(task);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Task> update(@PathVariable Long id, @RequestBody @Valid TaskDTO dto) {
        Task updatedTask = taskService.update(id, dto);
        return ResponseEntity.ok(updatedTask);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        taskService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/toggle")
    public ResponseEntity<Task> toggleCompleted(@PathVariable Long id) {
        Task task = taskService.toggleCompleted(id);
        return ResponseEntity.ok(task);
    }

    @GetMapping
    public ResponseEntity<List<Task>> filter(
            @RequestParam(required = false) Long subjectId,
            @RequestParam(required = false) Status status,
            @RequestParam(required = false) Priority priority) {

        List<Task> tasks = taskService.filter(subjectId, status, priority);
        return ResponseEntity.ok(tasks);
    }
}
