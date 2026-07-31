package com.example.kanbanTaskManager.auth;


import com.example.kanbanTaskManager.dto.AuthRequest;
import com.example.kanbanTaskManager.dto.AuthResponse;
import com.example.kanbanTaskManager.dto.RegisterRequest;
import com.example.kanbanTaskManager.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthService service;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register (@Valid @RequestBody RegisterRequest registerRequest) {

        return ResponseEntity.ok(service.register(registerRequest));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthResponse> register (@Valid@RequestBody AuthRequest request) {

        return ResponseEntity.ok(service.authenticate(request));
    }
}
