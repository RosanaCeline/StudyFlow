package com.studyflow.backend.data.dto;

import com.studyflow.backend.model.Priority;
import com.studyflow.backend.model.Status;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

@Schema(
        title = "DTO de Tarefas",
        description = "DTO utilizado para representar os dados de uma tarefa no StudyFlow."
)
public record TaskDTO(
        @NotBlank(message = "O título da tarefa é obrigatório")
        @Size(max = 60, message = "O título deve possuir no máximo 60 caracteres")
        @Schema(
                description = "Título da tarefa",
                example = "Criar tabela de users"
        )
        String title,

        @Size(max = 100, message = "A descrição deve possuir no máximo 100 caracteres")
        @Schema(
                description = "Descrição da tarefa",
                example = "A tabela deve conter nome, email, senha, idade e cpf do usuário",
                nullable = true
        )
        String description,

        @Schema(
                description = "Prazo para entrega da tarefa",
                example = "2026-09-03T12:30:00",
                nullable = true
        )
        LocalDateTime deadline,

        @NotNull(message = "A prioridade é obrigatória")
        @Schema(
                description = "Prioridade da tarefa",
                example = "HIGH"
        )
        Priority priority,

        @NotNull(message = "O status é obrigatório")
        @Schema(
                description = "Status atual da tarefa",
                example = "PENDING"
        )
        Status status,

        @NotNull(message = "A data de criação é obrigatória")
        @Schema(
                description = "Data e hora de criação da tarefa",
                example = "2026-09-03T10:30:00"
        )
        LocalDateTime creationDate,

        @NotNull(message = "A data de conclusão é obrigatória")
        @Schema(
                description = "Data e hora em que a tarefa foi concluída",
                example = "2026-09-03T10:30:00"
        )
        LocalDateTime completionDate
) {}
