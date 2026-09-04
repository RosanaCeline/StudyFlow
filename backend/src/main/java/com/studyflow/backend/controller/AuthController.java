package com.studyflow.backend.controller;

import com.studyflow.backend.controller.docs.AuthControllerDocs;
import com.studyflow.backend.data.dto.LoginDTO;
import com.studyflow.backend.data.dto.RegisterUserDTO;
import com.studyflow.backend.data.dto.TokenDTO;
import com.studyflow.backend.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController implements AuthControllerDocs {

    private final AuthService authService;

    @Override
    @PostMapping("/register")
    public ResponseEntity<TokenDTO> register(@RequestBody @Valid RegisterUserDTO dto) {
        TokenDTO tokenDto = authService.register(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(tokenDto);
    }

    @Override
    @PostMapping("/login")
    public ResponseEntity<TokenDTO> login(@RequestBody @Valid LoginDTO dto) {
        TokenDTO tokenDto = authService.login(dto);
        return ResponseEntity.ok(tokenDto);
    }
}