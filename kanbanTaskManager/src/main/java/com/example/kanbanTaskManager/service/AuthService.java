package com.example.kanbanTaskManager.service;

import com.example.kanbanTaskManager.config.JwtService;
import com.example.kanbanTaskManager.dto.AuthRequest;
import com.example.kanbanTaskManager.dto.AuthResponse;
import com.example.kanbanTaskManager.dto.RegisterRequest;
import com.example.kanbanTaskManager.enitiy.enums.AuthRole;
import com.example.kanbanTaskManager.enitiy.User;
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
                throw new IllegalArgumentException("Invalid role: " + registerRequest.getRole());
            }
        }


        User user = User.builder()
                .full_name(registerRequest.getFullName())
                .email(registerRequest.getEmail())
                .passwords(passwordEncoder.encode(registerRequest.getPassword()))
                .authRole(role)
                .created_at(LocalDateTime.now())
                .updated_at(LocalDateTime.now())
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
