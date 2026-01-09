package com.ethaum.startup.controller;

import com.ethaum.startup.dto.*;
import com.ethaum.startup.security.JwtHeaderUtil;
import com.ethaum.startup.service.StartupService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/startups")
public class StartupController {

    private final StartupService service;
    private final JwtHeaderUtil jwtUtil;

    public StartupController(
            StartupService service,
            JwtHeaderUtil jwtUtil
    ) {
        this.service = service;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping
    public ResponseEntity<StartupResponse> createStartup(
            @Valid @RequestBody StartupRequest req,
            @RequestHeader("Authorization") String authHeader
    ) {
        String token = authHeader.replace("Bearer ", "");
        String email = jwtUtil.extractEmail(token);

        return ResponseEntity.ok(
                service.createStartup(req, email)
        );
    }

    @GetMapping("/me")
    public ResponseEntity<StartupResponse> myStartup(
            @RequestHeader("Authorization") String authHeader
    ) {
        String token = authHeader.replace("Bearer ", "");
        String email = jwtUtil.extractEmail(token);

        return ResponseEntity.ok(
                service.getMyStartup(email)
        );
    }
}
