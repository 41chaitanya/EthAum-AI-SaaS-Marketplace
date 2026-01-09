package com.ethaum.launch.controller;

import com.ethaum.launch.dto.*;
import com.ethaum.launch.security.JwtHeaderUtil;
import com.ethaum.launch.service.LaunchService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/launches")
public class LaunchController {

    private final LaunchService service;
    private final JwtHeaderUtil jwtUtil;

    public LaunchController(
            LaunchService service,
            JwtHeaderUtil jwtUtil
    ) {
        this.service = service;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping
    public LaunchResponse launchProduct(
            @Valid @RequestBody LaunchRequest req,
            @RequestHeader("Authorization") String auth
    ) {
        String email = jwtUtil.extractEmail(auth.replace("Bearer ", ""));
        return service.createLaunch(req, email);
    }

    @PostMapping("/{id}/upvote")
    public void upvote(@PathVariable Long id) {
        service.upvote(id);
    }

    @GetMapping("/trending")
    public List<LaunchResponse> trending() {
        return service.trending();
    }
}
