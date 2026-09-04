package com.studyflow.backend.data.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "TokenResponseDTO", description = "DTO com token JWT retornado após autenticação")
public record TokenDTO(

        @Schema(description = "Nome do usuário autenticado", example = "Ana Clara")
        String name,

        @Schema(description = "ID do usuário autenticado", example = "12")
        Long id,

        @Schema(description = "Token JWT", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
        String token
) {}
