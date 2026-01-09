package com.ethaum.credibility.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TrustSignals {

    private int totalReviews;
    private double averageRating;
    private int enterpriseReviews;
    private int verifiedReviews;
    
    private int totalLaunches;
    private int totalUpvotes;
    private double avgViralityScore;
    
    private int videoTestimonials;
    private int caseStudies;
    
    private boolean hasEnterpriseBadge;
    private boolean hasFeaturedLaunch;
}
