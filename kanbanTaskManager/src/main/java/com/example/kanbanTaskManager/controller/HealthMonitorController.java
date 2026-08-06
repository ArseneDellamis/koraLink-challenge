package com.example.kanbanTaskManager.controller;


import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("/api/health")
@Tag(name = "Health Check", description = "Endpoints for monitoring application and database status")
public class HealthMonitorController {

    @GetMapping
    public ResponseEntity<Map<String,String>> heatjCheck() {

        return ResponseEntity.ok(
                Map.of(

                        "status", "up",
                        "service", "kanbanTaskManager",
                        "environment", "production",
                        "timestamp", LocalDateTime.now().toString()
                )
        );
    }
}
