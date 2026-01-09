package com.ethaum.credibility.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class ValidationReport {

    private Long startupId;
    private String startupName;
    private LocalDateTime generatedAt;
    
    // Credibility Summary
    private double credibilityScore;
    private String credibilityLevel;
    private String badge;
    
    // Quadrant Position
    private QuadrantPosition quadrantPosition;
    
    // Trust Signals
    private TrustSignals trustSignals;
    
    // Key Highlights
    private List<String> highlights;
    
    // Recommendation
    private String recommendation;
    
    // Report metadata
    private String reportId;
    private String reportType; // INVESTOR, ENTERPRISE, GENERAL
}
