package com.ethaum.review.controller;

import com.ethaum.review.dto.*;
import com.ethaum.review.security.JwtHeaderUtil;
import com.ethaum.review.service.TestimonialService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/testimonials")
public class TestimonialController {

    private final TestimonialService service;
    private final JwtHeaderUtil jwtUtil;

    public TestimonialController(TestimonialService service, JwtHeaderUtil jwtUtil) {
        this.service = service;
        this.jwtUtil = jwtUtil;
    }

    // Submit video or case study testimonial
    @PostMapping
    public TestimonialResponse submit(
            @RequestBody TestimonialRequest req,
            @RequestHeader("Authorization") String auth
    ) {
        String email = jwtUtil.extractEmail(auth.replace("Bearer ", ""));
        return service.submit(req, email);
    }

    // Get approved testimonials for a startup
    @GetMapping("/startup/{startupId}")
    public List<TestimonialResponse> getApproved(@PathVariable Long startupId) {
        return service.getApproved(startupId);
    }

    // Admin: Get pending testimonials
    @GetMapping("/pending")
    public List<TestimonialResponse> getPending(
            @RequestHeader("Authorization") String auth
    ) {
        String role = jwtUtil.extractRole(auth.replace("Bearer ", ""));
        if (!"ADMIN".equals(role)) {
            throw new RuntimeException("Admin access required");
        }
        return service.getPending();
    }

    // Admin: Moderate testimonial
    @PutMapping("/{id}/moderate")
    public TestimonialResponse moderate(
            @PathVariable Long id,
            @RequestParam String status,
            @RequestHeader("Authorization") String auth
    ) {
        String email = jwtUtil.extractEmail(auth.replace("Bearer ", ""));
        String role = jwtUtil.extractRole(auth.replace("Bearer ", ""));
        if (!"ADMIN".equals(role)) {
            throw new RuntimeException("Admin access required");
        }
        return service.moderate(id, status, email);
    }
}
