package com.ethaum.launch.service;

import com.ethaum.launch.dto.*;
import com.ethaum.launch.model.Launch;
import com.ethaum.launch.repository.LaunchRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class LaunchService {

    private final LaunchRepository repo;
    private final ViralityService viralityService;
    private final BadgeService badgeService;

    public LaunchService(
            LaunchRepository repo,
            ViralityService viralityService,
            BadgeService badgeService
    ) {
        this.repo = repo;
        this.viralityService = viralityService;
        this.badgeService = badgeService;
    }

    public LaunchResponse createLaunch(LaunchRequest req, String ownerEmail) {
        Launch launch = Launch.builder()
                .startupId(req.getStartupId())
                .productName(req.getProductName())
                .tagline(req.getTagline())
                .description(req.getDescription())
                .category(req.getCategory())
                .upvotes(0)
                .commentsCount(0)
                .viewCount(0)
                .ownerEmail(ownerEmail)
                .launchedAt(LocalDateTime.now())
                .viralityScore(0.0)
                .trending(false)
                .aiAssisted(false)
                .badge("EthAum Verified")
                .badgeAssignedAt(LocalDateTime.now())
                .build();

        Launch saved = repo.save(launch);
        return map(saved);
    }

    public LaunchResponse createLaunchWithAI(
            LaunchRequest req, 
            String ownerEmail,
            String generatedTagline,
            String generatedDescription
    ) {
        Launch launch = Launch.builder()
                .startupId(req.getStartupId())
                .productName(req.getProductName())
                .tagline(generatedTagline)
                .description(generatedDescription)
                .category(req.getCategory())
                .upvotes(0)
                .commentsCount(0)
                .viewCount(0)
                .ownerEmail(ownerEmail)
                .launchedAt(LocalDateTime.now())
                .viralityScore(0.0)
                .trending(false)
                .aiAssisted(true)
                .generatedTagline(generatedTagline)
                .generatedDescription(generatedDescription)
                .badge("EthAum Verified")
                .badgeAssignedAt(LocalDateTime.now())
                .build();

        Launch saved = repo.save(launch);
        return map(saved);
    }

    public void upvote(Long launchId) {
        Launch launch = repo.findById(launchId)
                .orElseThrow(() -> new RuntimeException("Launch not found"));
        launch.setUpvotes(launch.getUpvotes() + 1);
        
        // Recalculate virality and badge
        viralityService.updateViralityScore(launch);
        badgeService.updateBadge(launch);
        
        repo.save(launch);
    }

    public void trackView(Long launchId) {
        Launch launch = repo.findById(launchId)
                .orElseThrow(() -> new RuntimeException("Launch not found"));
        launch.setViewCount(launch.getViewCount() + 1);
        
        // Recalculate virality
        viralityService.updateViralityScore(launch);
        badgeService.updateBadge(launch);
        
        repo.save(launch);
    }

    public ViralityResponse getAnalytics(Long launchId) {
        Launch launch = repo.findById(launchId)
                .orElseThrow(() -> new RuntimeException("Launch not found"));
        
        // Update score
        viralityService.updateViralityScore(launch);
        badgeService.updateBadge(launch);
        repo.save(launch);

        return ViralityResponse.builder()
                .launchId(launchId)
                .viralityScore(launch.getViralityScore())
                .trending(launch.isTrending())
                .upvotes(launch.getUpvotes())
                .commentsCount(launch.getCommentsCount())
                .viewCount(launch.getViewCount())
                .badge(launch.getBadge())
                .build();
    }

    public LaunchResponse getLaunch(Long launchId) {
        Launch launch = repo.findById(launchId)
                .orElseThrow(() -> new RuntimeException("Launch not found"));
        return map(launch);
    }

    public List<LaunchResponse> trending() {
        return repo.findByTrendingTrueOrderByViralityScoreDesc()
                .stream()
                .map(this::map)
                .toList();
    }

    public List<LaunchResponse> featured() {
        return repo.findByBadgeOrderByViralityScoreDesc("Featured Launch")
                .stream()
                .map(this::map)
                .toList();
    }

    public List<LaunchResponse> topLaunches() {
        return repo.findAllByOrderByViralityScoreDesc()
                .stream()
                .limit(10)
                .map(this::map)
                .toList();
    }

    private LaunchResponse map(Launch l) {
        return LaunchResponse.builder()
                .id(l.getId())
                .productName(l.getProductName())
                .tagline(l.getTagline())
                .description(l.getDescription())
                .category(l.getCategory())
                .upvotes(l.getUpvotes())
                .viewCount(l.getViewCount())
                .viralityScore(l.getViralityScore())
                .trending(l.isTrending())
                .badge(l.getBadge())
                .aiAssisted(l.isAiAssisted())
                .build();
    }
}
