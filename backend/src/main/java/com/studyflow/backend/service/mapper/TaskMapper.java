package com.studyflow.backend.service.mapper;

import com.studyflow.backend.data.dto.TaskDTO;
import com.studyflow.backend.model.Task;
import org.springframework.stereotype.Component;

@Component
public class TaskMapper {

    public TaskDTO toDTO(Task task) {
        return new TaskDTO(
                task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.getDeadline(),
                task.getPriority(),
                task.getStatus(),
                task.getSubject().getId()
        );
    }
}
