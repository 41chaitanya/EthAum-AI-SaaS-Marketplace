package com.ethaum.review.controller;

import com.ethaum.review.dto.*;
import com.ethaum.review.security.JwtHeaderUtil;
import com.ethaum.review.service.ReviewService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reviews")
public class ReviewController {

    private final ReviewService service;
    private final JwtHeaderUtil jwtUtil;

    public ReviewController(
            ReviewService service,
            JwtHeaderUtil jwtUtil
    ) {
        this.service = service;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping
    public ReviewResponse addReview(
            @Valid @RequestBody ReviewRequest req,
            @RequestHeader("Authorization") String auth
    ) {
        String email = jwtUtil.extractEmail(auth.replace("Bearer ", ""));
        return service.addReview(req, email);
    }

    @GetMapping("/launch/{launchId}")
    public List<ReviewResponse> reviews(@PathVariable Long launchId) {
        return service.getReviews(launchId);
    }

    // Get only verified reviews
    @GetMapping("/launch/{launchId}/verified")
    public List<ReviewResponse> verifiedReviews(@PathVariable Long launchId) {
        return service.getVerifiedReviews(launchId);
    }

    // Re-verify a specific review
    @PostMapping("/{reviewId}/verify")
    public VerificationResult verifyReview(@PathVariable Long reviewId) {
        return service.verifyReview(reviewId);
    }
}
