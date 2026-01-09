package com.ethaum.launch.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LaunchResponse {
    private Long id;
    private String productName;
    private String tagline;
    private String description;
    private String category;
    private int upvotes;
    private int viewCount;
    
    // Virality fields
    private double viralityScore;
    private boolean trending;
    
    // Badge
    private String badge;
    
    // AI assisted
    private boolean aiAssisted;
}
