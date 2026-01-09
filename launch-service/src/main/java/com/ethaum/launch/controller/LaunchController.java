package com.ethaum.launch.controller;

import com.ethaum.launch.dto.*;
import com.ethaum.launch.security.JwtHeaderUtil;
import com.ethaum.launch.service.AITemplateService;
import com.ethaum.launch.service.LaunchService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/launches")
public class LaunchController {

    private final LaunchService service;
    private final AITemplateService aiTemplateService;
    private final JwtHeaderUtil jwtUtil;

    public LaunchController(
            LaunchService service,
            AITemplateService aiTemplateService,
            JwtHeaderUtil jwtUtil
    ) {
        this.service = service;
        this.aiTemplateService = aiTemplateService;
        this.jwtUtil = jwtUtil;
    }

    // ========== AI TEMPLATES ==========

    @PostMapping("/templates/generate")
    public TemplateResponse generateTemplate(@RequestBody TemplateRequest req) {
        return aiTemplateService.generateTemplates(req);
    }

    // ========== LAUNCH CRUD ==========

    @PostMapping
    public LaunchResponse launchProduct(
            @Valid @RequestBody LaunchRequest req,
            @RequestHeader("Authorization") String auth
    ) {
        String email = jwtUtil.extractEmail(auth.replace("Bearer ", ""));
        return service.createLaunch(req, email);
    }

    @PostMapping("/with-ai")
    public LaunchResponse launchWithAI(
            @Valid @RequestBody LaunchRequest req,
            @RequestHeader("Authorization") String auth
    ) {
        String email = jwtUtil.extractEmail(auth.replace("Bearer ", ""));
        
        // Generate AI content
        TemplateRequest templateReq = new TemplateRequest();
        templateReq.setProductName(req.getProductName());
        templateReq.setCategory(req.getCategory());
        templateReq.setIndustry("B2B");
        templateReq.setProblemSolved(req.getDescription());
        
        TemplateResponse templates = aiTemplateService.generateTemplates(templateReq);
        
        return service.createLaunchWithAI(
                req, 
                email,
                templates.getSuggestedTagline(),
                templates.getSuggestedDescription()
        );
    }

    @GetMapping("/{id}")
    public LaunchResponse getLaunch(@PathVariable Long id) {
        return service.getLaunch(id);
    }

    // ========== ENGAGEMENT ==========

    @PostMapping("/{id}/upvote")
    public void upvote(@PathVariable Long id) {
        service.upvote(id);
    }

    @PostMapping("/{id}/view")
    public void trackView(@PathVariable Long id) {
        service.trackView(id);
    }

    // ========== ANALYTICS ==========

    @GetMapping("/{id}/analytics")
    public ViralityResponse getAnalytics(@PathVariable Long id) {
        return service.getAnalytics(id);
    }

    // ========== LISTINGS ==========

    @GetMapping("/trending")
    public List<LaunchResponse> trending() {
        return service.trending();
    }

    @GetMapping("/featured")
    public List<LaunchResponse> featured() {
        return service.featured();
    }

    @GetMapping("/top")
    public List<LaunchResponse> topLaunches() {
        return service.topLaunches();
    }
}
