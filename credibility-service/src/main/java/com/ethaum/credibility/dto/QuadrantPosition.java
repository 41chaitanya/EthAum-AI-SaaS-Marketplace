package com.ethaum.credibility.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class QuadrantPosition {

    private Long startupId;
    private String startupName;
    
    // X-axis: Market Traction (0-100)
    private double marketTraction;
    
    // Y-axis: Trust & Credibility (0-100)
    private double trustScore;
    
    // Quadrant: NICHE_PLAYER, VISIONARY, CHALLENGER, LEADER
    private String quadrant;
    
    // Position description
    private String description;
    
    // Raw metrics used
    private int totalUpvotes;
    private int launchCount;
    private double avgRating;
    private int enterpriseReviews;
    private double viralityScore;
}
