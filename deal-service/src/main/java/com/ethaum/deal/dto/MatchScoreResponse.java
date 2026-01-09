package com.ethaum.deal.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class MatchScoreResponse {

    private Long dealId;
    private Long applicationId;
    private String enterpriseEmail;
    
    // Match score 0-100
    private int matchScore;
    
    // POOR_MATCH, GOOD_MATCH, STRONG_MATCH
    private String matchLabel;
    
    // Breakdown
    private int industryScore;
    private int credibilityScore;
    private int companySizeScore;
    
    // Explainability
    private List<String> matchReasons;
}
