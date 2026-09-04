package com.studyflow.backend.data.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(
        title = "DTO de Cadastro de Usuário",
        description = "DTO utilizado para cadastrar um perfil de usuário."
)
public record RegisterUserDTO(
        @Schema(description = "Nome completo do profissional", example = "Ana Clara Silva")
        @NotBlank(message = "Nome é obrigatório.")
        @Size(max = 150, message = "Nome deve ter no máximo 150 caracteres.")
        String name,

        @Schema(description = "Email para login", example = "ana.clara@email.com")
        @NotBlank(message = "Email é obrigatório.")
        @Email(message = "Email inválido.")
        @Size(max = 255, message = "Email deve ter no máximo 255 caracteres.")
        String email,

        @Schema(description = "Senha de acesso ", example = "senhaForte123")
        @NotBlank(message = "Senha é obrigatória.")
        @Size(max = 70, message = "Senha deve ter no máximo 70 caracteres.")
        String password
) {}
