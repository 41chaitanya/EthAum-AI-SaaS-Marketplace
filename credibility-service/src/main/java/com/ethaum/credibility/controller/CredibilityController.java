package com.ethaum.credibility.controller;

import com.ethaum.credibility.dto.CredibilityResponse;
import com.ethaum.credibility.service.CredibilityService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/credibility")
public class CredibilityController {

    private final CredibilityService service;

    public CredibilityController(CredibilityService service) {
        this.service = service;
    }

    /**
     * MVP API
     * Normally this data will come from Launch + Review services
     */
    @PostMapping("/calculate")
    public CredibilityResponse calculate(
            @RequestParam Long startupId,
            @RequestParam int launchUpvotes,
            @RequestParam double avgRating,
            @RequestParam int enterpriseReviews
    ) {
        return service.calculateCredibility(
                startupId,
                launchUpvotes,
                avgRating,
                enterpriseReviews
        );
    }

    @GetMapping("/startup/{startupId}")
    public CredibilityResponse get(@PathVariable Long startupId) {
        return service.getCredibility(startupId);
    }
}
