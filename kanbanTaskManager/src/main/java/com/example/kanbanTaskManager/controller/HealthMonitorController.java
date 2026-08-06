package com.example.kanbanTaskManager.controller;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/health")
public class HealthMonitorController {

    @GetMapping
    public ResponseEntity<Map<String,String>> heatjCheck() {

        return ResponseEntity.ok(
                Map.of(

                        "status", "up",
                        "service", "kanbanTaskManager",
                        "environment", "production"
                )
        );
    }
}
