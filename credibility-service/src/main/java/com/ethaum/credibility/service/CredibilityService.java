package com.ethaum.credibility.service;

import com.ethaum.credibility.dto.CredibilityResponse;
import com.ethaum.credibility.model.CredibilityScore;
import com.ethaum.credibility.repository.CredibilityRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class CredibilityService {

    private final CredibilityRepository repo;

    public CredibilityService(CredibilityRepository repo) {
        this.repo = repo;
    }

    public CredibilityResponse calculateCredibility(
            Long startupId,
            int launchUpvotes,
            double avgRating,
            int enterpriseReviews
    ) {

        // ---- CORE FORMULA (EthAum USP) ----
        double score =
                (launchUpvotes * 0.3)
              + (avgRating * 10)
              + (enterpriseReviews * 15);

        if (score > 100) score = 100;

        String level;
        String badge;

        if (score <= 30) {
            level = "Emerging";
            badge = "EthAum Verified";
        } else if (score <= 60) {
            level = "Growing";
            badge = "High Growth Startup";
        } else {
            level = "Enterprise Ready";
            badge = "Enterprise Ready";
        }

        CredibilityScore credibility = CredibilityScore.builder()
                .startupId(startupId)
                .score(score)
                .level(level)
                .badge(badge)
                .calculatedAt(LocalDateTime.now())
                .build();

        repo.save(credibility);

        return CredibilityResponse.builder()
                .startupId(startupId)
                .score(score)
                .level(level)
                .badge(badge)
                .build();
    }

    public CredibilityResponse getCredibility(Long startupId) {

        CredibilityScore c = repo.findByStartupId(startupId)
                .orElseThrow(() -> new RuntimeException("Credibility not found"));

        return CredibilityResponse.builder()
                .startupId(c.getStartupId())
                .score(c.getScore())
                .level(c.getLevel())
                .badge(c.getBadge())
                .build();
    }
}
