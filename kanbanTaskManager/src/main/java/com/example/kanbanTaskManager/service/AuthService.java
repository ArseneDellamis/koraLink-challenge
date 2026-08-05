package com.example.kanbanTaskManager.service;

import com.example.kanbanTaskManager.config.JwtService;
import com.example.kanbanTaskManager.dto.AuthRequest;
import com.example.kanbanTaskManager.dto.AuthResponse;
import com.example.kanbanTaskManager.dto.RegisterRequest;
import com.example.kanbanTaskManager.enitiy.enums.AuthRole;
import com.example.kanbanTaskManager.enitiy.User;
import com.example.kanbanTaskManager.exceptionHandler.BadRequestException;
import com.example.kanbanTaskManager.exceptionHandler.DuplicateResourceException;
import com.example.kanbanTaskManager.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager manager;

    public AuthResponse register(RegisterRequest registerRequest) {

        AuthRole role;

        if (registerRequest.getRole() == null || registerRequest.getRole().isBlank()) {
            role = AuthRole.USER;
        } else {
            try {
                role = AuthRole.valueOf(registerRequest.getRole().trim().toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new BadRequestException("Invalid role: " + registerRequest.getRole());
            }
        }

        if (!registerRequest.getConfirmPassword().equals(registerRequest.getPassword())){
            throw new BadRequestException("passwords don't match");
        }
        if (repository.existsByEmail(
                registerRequest.getEmail()
        )){

            throw new DuplicateResourceException("Email already registered");
        }


        User user = User.builder()
                .fullName(registerRequest.getFullName())
                .email(registerRequest.getEmail())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .authRole(role)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        repository.save(user);

        var jwtToken = jwtService.generateToken(user);

        return AuthResponse.builder()
                .token(jwtToken)
                .build();
    }

    public AuthResponse authenticate(AuthRequest request) {

        manager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        User user = repository.findByEmail(request.getEmail()).orElseThrow();
        var jwtToken = jwtService.generateToken(user);
        return AuthResponse.builder()
                .token(jwtToken)
                .build();
    }
}
