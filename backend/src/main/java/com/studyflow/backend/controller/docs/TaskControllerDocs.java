package com.studyflow.backend.controller.docs;

import com.studyflow.backend.data.dto.TaskDTO;
import com.studyflow.backend.model.Priority;
import com.studyflow.backend.model.Status;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Tasks", description = "Endpoints para gerenciamento de tarefas")
public interface TaskControllerDocs {

    @Operation(summary = "Cria uma nova tarefa", description = "Cadastra uma tarefa vinculada a uma disciplina existente.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Tarefa criada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados de entrada inválidos"),
            @ApiResponse(responseCode = "404", description = "Disciplina não encontrada")
    })
    @PostMapping
    ResponseEntity<TaskDTO> create(@RequestBody @Valid TaskDTO dto);

    @Operation(summary = "Atualiza uma tarefa", description = "Atualiza todos os dados de uma tarefa existente pelo seu ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tarefa atualizada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados de entrada inválidos"),
            @ApiResponse(responseCode = "404", description = "Tarefa ou disciplina não encontrada")
    })
    @PutMapping("/{id}")
    ResponseEntity<TaskDTO> update(
            @Parameter(description = "ID da tarefa", required = true) @PathVariable Long id,
            @RequestBody @Valid TaskDTO dto
    );

    @Operation(summary = "Deleta uma tarefa", description = "Remove permanentemente uma tarefa do sistema pelo seu ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Tarefa deletada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Tarefa não encontrada")
    })
    @DeleteMapping("/{id}")
    ResponseEntity<Void> delete(
            @Parameter(description = "ID da tarefa", required = true) @PathVariable Long id
    );

    @Operation(summary = "Alterna o status de conclusão da tarefa", description = "Alterna entre COMPLETED e PENDING, atualizando automaticamente a data de conclusão.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Status alternado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Tarefa não encontrada")
    })
    @PatchMapping("/{id}/toggle")
    ResponseEntity<TaskDTO> toggleCompleted(
            @Parameter(description = "ID da tarefa", required = true) @PathVariable Long id
    );

    @Operation(summary = "Lista e filtra tarefas", description = "Retorna uma lista de tarefas. Permite filtrar por disciplina, status e prioridade.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de tarefas retornada com sucesso")
    })
    @GetMapping
    ResponseEntity<List<TaskDTO>> filter(
            @Parameter(description = "ID da disciplina para filtro") @RequestParam(required = false) Long subjectId,
            @Parameter(description = "Status da tarefa (PENDING, COMPLETED, etc.)") @RequestParam(required = false) Status status,
            @Parameter(description = "Prioridade da tarefa (LOW, MEDIUM, HIGH, etc.)") @RequestParam(required = false) Priority priority
    );
}