package com.studyflow.backend.controller.docs;

import com.studyflow.backend.data.dto.LoginDTO;
import com.studyflow.backend.data.dto.RegisterUserDTO;
import com.studyflow.backend.data.dto.TokenDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "Autenticação", description = "Endpoints para cadastro e login de usuários")
public interface AuthControllerDocs {

    @Operation(summary = "Cadastra um novo usuário", description = "Cria a conta do usuário e já retorna o token de autenticação.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Usuário cadastrado com sucesso e autenticado"),
            @ApiResponse(responseCode = "400", description = "Dados de entrada inválidos"),
            @ApiResponse(responseCode = "409", description = "E-mail já cadastrado no sistema")
    })
    @PostMapping("/register")
    ResponseEntity<TokenDTO> register(@RequestBody @Valid RegisterUserDTO dto);

    @Operation(summary = "Realiza o login", description = "Autentica as credenciais do usuário e retorna o token JWT.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Autenticação realizada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados de entrada inválidos"),
            @ApiResponse(responseCode = "401", description = "Credenciais inválidas (senha incorreta)"),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
    })
    @PostMapping("/login")
    ResponseEntity<TokenDTO> login(@RequestBody @Valid LoginDTO dto);
}