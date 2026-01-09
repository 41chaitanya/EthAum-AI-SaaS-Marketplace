package com.ethaum.credibility.service;

import com.ethaum.credibility.dto.*;
import com.ethaum.credibility.model.CredibilitySnapshot;
import com.ethaum.credibility.repository.CredibilitySnapshotRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class TrendService {

    private final CredibilitySnapshotRepository snapshotRepo;

    public TrendService(CredibilitySnapshotRepository snapshotRepo) {
        this.snapshotRepo = snapshotRepo;
    }

    public TrendDashboard getTrend(Long startupId, String granularity) {
        LocalDate endDate = LocalDate.now();
        LocalDate startDate = "MONTHLY".equals(granularity) 
                ? endDate.minusMonths(6) 
                : endDate.minusWeeks(8);

        List<CredibilitySnapshot> snapshots = snapshotRepo
                .findByStartupIdAndSnapshotDateBetweenOrderBySnapshotDateAsc(startupId, startDate, endDate);

        List<TrendDataPoint> dataPoints = snapshots.stream()
                .map(s -> TrendDataPoint.builder()
                        .date(s.getSnapshotDate())
                        .credibilityScore(s.getCredibilityScore())
                        .upvotes(s.getTotalUpvotes())
                        .reviews(s.getTotalReviews())
                        .viralityScore(s.getViralityScore())
                        .build())
                .toList();

        double currentScore = dataPoints.isEmpty() ? 0 : 
                dataPoints.get(dataPoints.size() - 1).getCredibilityScore();
        double firstScore = dataPoints.isEmpty() ? 0 : 
                dataPoints.get(0).getCredibilityScore();
        
        double scoreChange = firstScore > 0 
                ? ((currentScore - firstScore) / firstScore) * 100 
                : 0;

        String trend = determineTrend(scoreChange);
        String insight = generateInsight(trend, scoreChange, currentScore);

        return TrendDashboard.builder()
                .startupId(startupId)
                .granularity(granularity)
                .dataPoints(dataPoints)
                .currentScore(currentScore)
                .scoreChange(Math.round(scoreChange * 10.0) / 10.0)
                .trend(trend)
                .insight(insight)
                .build();
    }

    public void recordSnapshot(Long startupId, double credibilityScore, 
            int upvotes, int reviews, double avgRating, 
            int enterpriseReviews, double viralityScore, int launchCount) {
        
        LocalDate today = LocalDate.now();
        
        // Check if snapshot exists for today
        var existing = snapshotRepo.findByStartupIdAndSnapshotDate(startupId, today);
        
        CredibilitySnapshot snapshot;
        if (existing.isPresent()) {
            snapshot = existing.get();
        } else {
            snapshot = new CredibilitySnapshot();
            snapshot.setStartupId(startupId);
            snapshot.setSnapshotDate(today);
        }
        
        snapshot.setCredibilityScore(credibilityScore);
        snapshot.setTotalUpvotes(upvotes);
        snapshot.setTotalReviews(reviews);
        snapshot.setAvgRating(avgRating);
        snapshot.setEnterpriseReviews(enterpriseReviews);
        snapshot.setViralityScore(viralityScore);
        snapshot.setLaunchCount(launchCount);
        
        snapshotRepo.save(snapshot);
    }

    private String determineTrend(double scoreChange) {
        if (scoreChange > 10) return "RISING";
        if (scoreChange < -10) return "DECLINING";
        return "STABLE";
    }

    private String generateInsight(String trend, double change, double currentScore) {
        return switch (trend) {
            case "RISING" -> String.format(
                "Strong growth! Credibility increased by %.1f%%. Current score: %.1f", 
                change, currentScore);
            case "DECLINING" -> String.format(
                "Attention needed. Credibility decreased by %.1f%%. Focus on reviews and engagement.", 
                Math.abs(change));
            default -> String.format(
                "Stable performance with score of %.1f. Consider new launches to boost visibility.", 
                currentScore);
        };
    }
}
