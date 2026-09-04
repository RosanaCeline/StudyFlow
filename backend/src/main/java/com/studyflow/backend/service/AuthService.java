package com.studyflow.backend.service;

import com.studyflow.backend.data.dto.LoginDTO;
import com.studyflow.backend.data.dto.RegisterUserDTO;
import com.studyflow.backend.data.dto.TokenDTO;
import com.studyflow.backend.exception.EmailAlreadyRegisteredException;
import com.studyflow.backend.exception.InvalidPasswordException;
import com.studyflow.backend.exception.UserNotFoundException;
import com.studyflow.backend.model.User;
import com.studyflow.backend.repository.UserRepository;
import com.studyflow.backend.security.TokenService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;

    @Transactional
    public TokenDTO register(RegisterUserDTO dto) {
        if (userRepository.findByEmail(dto.email()).isPresent()) {
            throw new EmailAlreadyRegisteredException();
        }

        User newUser = new User();
        newUser.setName(dto.name());
        newUser.setEmail(dto.email());
        newUser.setPassword(passwordEncoder.encode(dto.password()));

        userRepository.save(newUser);

        String token = tokenService.generateToken(
                newUser.getUsername(),
                newUser.getId(),
                newUser.getName().split(" ")[0],
                "login-auth-api",
                2
        );

        return new TokenDTO(newUser.getName(), newUser.getId(), token);
    }

    public TokenDTO login(LoginDTO dto) {
        User user = this.userRepository.findByEmail(dto.email())
                .orElseThrow(UserNotFoundException::new);

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(dto.email(), dto.password())
            );

            String token = tokenService.generateToken(
                    user.getEmail(),
                    user.getId(),
                    user.getName().split(" ")[0],
                    "login-auth-api",
                    2
            );

            return new TokenDTO(user.getName(), user.getId(), token);

        } catch (BadCredentialsException e) {
            throw new InvalidPasswordException();
        }
    }
}

