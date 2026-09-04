package com.studyflow.backend.controller.docs;

import com.studyflow.backend.data.dto.SubjectDTO;
import com.studyflow.backend.model.Subject;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Subjects", description = "Endpoints para gerenciamento de disciplinas")
public interface SubjectControllerDocs {

    @Operation(summary = "Cria uma nova disciplina", description = "Cadastra uma nova disciplina com nome, descrição, cor e situação.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Disciplina criada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados de entrada inválidos")
    })
    @PostMapping
    ResponseEntity<Subject> create(@RequestBody @Valid SubjectDTO dto);

    @Operation(summary = "Atualiza uma disciplina", description = "Atualiza todos os dados de uma disciplina existente pelo seu ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Disciplina atualizada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados de entrada inválidos"),
            @ApiResponse(responseCode = "404", description = "Disciplina não encontrada")
    })
    @PutMapping("/{id}")
    ResponseEntity<Subject> update(
            @Parameter(description = "ID da disciplina", required = true) @PathVariable Long id,
            @RequestBody @Valid SubjectDTO dto
    );

    @Operation(summary = "Deleta uma disciplina", description = "Remove permanentemente uma disciplina do sistema pelo seu ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Disciplina deletada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Disciplina não encontrada")
    })
    @DeleteMapping("/{id}")
    ResponseEntity<Void> delete(
            @Parameter(description = "ID da disciplina", required = true) @PathVariable Long id
    );

    @Operation(summary = "Lista todas as disciplinas", description = "Retorna uma lista com todas as disciplinas cadastradas.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de disciplinas retornada com sucesso")
    })
    @GetMapping
    ResponseEntity<List<Subject>> listAll();
}