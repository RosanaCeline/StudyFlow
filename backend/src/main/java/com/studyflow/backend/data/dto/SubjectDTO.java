package com.studyflow.backend.data.dto;


import com.studyflow.backend.model.Situation;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;
import java.util.List;

@Schema(
        title = "DTO de Discplina",
        description = "DTO utilizado para representar os dados de uma disciplina no StudyFlow."
)
public record SubjectDTO(
        @Schema(
                description = "ID da disciplina",
                example = "1"
        )
        Long id,

        @NotBlank(message = "O nome da disciplina é obrigatório")
        @Size(max = 100, message = "O nome deve possuir no máximo 100 caracteres")
        @Schema(
                description = "Nome da disciplina ou categoria de estudo",
                example = "Banco de Dados"
        )
        String name,

        @Size(max = 255, message = "A descrição deve possuir no máximo 255 caracteres")
        @Schema(
                description = "Descrição da disciplina",
                example = "Estudos relacionados a PostgreSQL e modelagem de dados",
                nullable = true
        )
        String description,

        @NotBlank(message = "A cor da disciplina é obrigatória")
        @Size(max = 20, message = "A cor deve possuir no máximo 20 caracteres")
        @Schema(
                description = "Cor utilizada para identificação visual da disciplina",
                example = "#4F46E5"
        )
        String color,

        @NotNull(message = "A situação é obrigatória")
        @Schema(
                description = "Situação atual da disciplina",
                example = "ACTIVE"
        )
        Situation situation,

        @Schema(
                description = "Lista de tarefas vinculadas à disciplina"
        )
        List<TaskDTO> task,

        @Schema(
                description = "Data de criação da disciplina"
        )
        LocalDateTime creationDate
) {}
