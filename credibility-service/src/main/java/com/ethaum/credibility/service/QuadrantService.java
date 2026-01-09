package com.ethaum.credibility.service;

import com.ethaum.credibility.dto.QuadrantPosition;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Gartner-style Credibility Quadrant
 * 
 * X-axis: Market Traction (launch activity, upvotes, engagement)
 * Y-axis: Trust & Credibility (reviews, enterprise validation)
 * 
 * Quadrants:
 * - NICHE_PLAYER: Low traction, Low trust (bottom-left)
 * - VISIONARY: Low traction, High trust (top-left)
 * - CHALLENGER: High traction, Low trust (bottom-right)
 * - LEADER: High traction, High trust (top-right)
 */
@Service
public class QuadrantService {

    private static final double TRACTION_THRESHOLD = 50.0;
    private static final double TRUST_THRESHOLD = 50.0;

    public QuadrantPosition calculatePosition(
            Long startupId,
            String startupName,
            int totalUpvotes,
            int launchCount,
            double viralityScore,
            double avgRating,
            int totalReviews,
            int enterpriseReviews
    ) {
        // Calculate Market Traction (X-axis: 0-100)
        double marketTraction = calculateMarketTraction(totalUpvotes, launchCount, viralityScore);
        
        // Calculate Trust Score (Y-axis: 0-100)
        double trustScore = calculateTrustScore(avgRating, totalReviews, enterpriseReviews);
        
        // Determine quadrant
        String quadrant = determineQuadrant(marketTraction, trustScore);
        String description = getQuadrantDescription(quadrant);

        return QuadrantPosition.builder()
                .startupId(startupId)
                .startupName(startupName)
                .marketTraction(round(marketTraction))
                .trustScore(round(trustScore))
                .quadrant(quadrant)
                .description(description)
                .totalUpvotes(totalUpvotes)
                .launchCount(launchCount)
                .avgRating(avgRating)
                .enterpriseReviews(enterpriseReviews)
                .viralityScore(viralityScore)
                .build();
    }

    /**
     * Market Traction Formula:
     * - Upvotes: 40% weight (normalized to 100 at 500 upvotes)
     * - Launch count: 20% weight (normalized to 100 at 5 launches)
     * - Virality score: 40% weight (already 0-100)
     */
    private double calculateMarketTraction(int upvotes, int launchCount, double viralityScore) {
        double upvoteScore = Math.min(100, (upvotes / 500.0) * 100);
        double launchScore = Math.min(100, (launchCount / 5.0) * 100);
        
        return (upvoteScore * 0.4) + (launchScore * 0.2) + (viralityScore * 0.4);
    }

    /**
     * Trust Score Formula:
     * - Average rating: 40% weight (scaled from 1-5 to 0-100)
     * - Total reviews: 30% weight (normalized to 100 at 50 reviews)
     * - Enterprise reviews: 30% weight (normalized to 100 at 10 enterprise reviews)
     */
    private double calculateTrustScore(double avgRating, int totalReviews, int enterpriseReviews) {
        double ratingScore = ((avgRating - 1) / 4.0) * 100; // 1-5 -> 0-100
        double reviewScore = Math.min(100, (totalReviews / 50.0) * 100);
        double enterpriseScore = Math.min(100, (enterpriseReviews / 10.0) * 100);
        
        return (ratingScore * 0.4) + (reviewScore * 0.3) + (enterpriseScore * 0.3);
    }

    private String determineQuadrant(double marketTraction, double trustScore) {
        if (marketTraction >= TRACTION_THRESHOLD && trustScore >= TRUST_THRESHOLD) {
            return "LEADER";
        } else if (marketTraction >= TRACTION_THRESHOLD && trustScore < TRUST_THRESHOLD) {
            return "CHALLENGER";
        } else if (marketTraction < TRACTION_THRESHOLD && trustScore >= TRUST_THRESHOLD) {
            return "VISIONARY";
        } else {
            return "NICHE_PLAYER";
        }
    }

    private String getQuadrantDescription(String quadrant) {
        return switch (quadrant) {
            case "LEADER" -> "High market traction with strong enterprise trust. Ready for scale.";
            case "CHALLENGER" -> "Strong market presence but needs more enterprise validation.";
            case "VISIONARY" -> "Trusted by enterprises but needs broader market adoption.";
            case "NICHE_PLAYER" -> "Early stage startup building initial traction and trust.";
            default -> "Position not determined";
        };
    }

    private double round(double value) {
        return Math.round(value * 10.0) / 10.0;
    }

    /**
     * Get all startups positioned in quadrant for comparison
     */
    public List<QuadrantPosition> getQuadrantComparison(List<QuadrantPosition> positions) {
        return positions.stream()
                .sorted((a, b) -> {
                    // Sort by combined score (traction + trust)
                    double scoreA = a.getMarketTraction() + a.getTrustScore();
                    double scoreB = b.getMarketTraction() + b.getTrustScore();
                    return Double.compare(scoreB, scoreA);
                })
                .toList();
    }
}
