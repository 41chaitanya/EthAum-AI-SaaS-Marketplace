package com.ethaum.launch.service;

import com.ethaum.launch.model.Launch;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Service
public class ViralityService {

    // Weights for MVP formula
    private static final double UPVOTE_WEIGHT = 2.0;
    private static final double COMMENT_WEIGHT = 3.0;
    private static final double VIEW_WEIGHT = 0.1;
    private static final double RECENCY_DECAY = 0.95; // per day
    private static final double TRENDING_THRESHOLD = 50.0;

    public double calculateViralityScore(Launch launch) {
        if (launch.getLaunchedAt() == null) {
            return 0.0;
        }

        long hoursSinceLaunch = ChronoUnit.HOURS.between(
            launch.getLaunchedAt(), 
            LocalDateTime.now()
        );
        
        double daysSinceLaunch = Math.max(hoursSinceLaunch / 24.0, 0.1);
        double recencyMultiplier = Math.pow(RECENCY_DECAY, daysSinceLaunch);
        
        double rawScore = 
            (launch.getUpvotes() * UPVOTE_WEIGHT) +
            (launch.getCommentsCount() * COMMENT_WEIGHT) +
            (launch.getViewCount() * VIEW_WEIGHT);
        
        double finalScore = rawScore * recencyMultiplier;
        
        // Cap at 100
        return Math.min(100, Math.round(finalScore * 10.0) / 10.0);
    }

    public boolean isTrending(double viralityScore) {
        return viralityScore >= TRENDING_THRESHOLD;
    }

    public void updateViralityScore(Launch launch) {
        double score = calculateViralityScore(launch);
        launch.setViralityScore(score);
        launch.setTrending(isTrending(score));
        launch.setLastScoreUpdate(LocalDateTime.now());
    }

    public void updateLaunchTrending(Launch launch, boolean trending) {
        launch.setTrending(trending);
    }
}
