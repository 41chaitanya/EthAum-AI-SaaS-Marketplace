package com.ethaum.deal.service;

import com.ethaum.deal.dto.MatchScoreResponse;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * AI Matchmaking Service - Rule-based MVP
 * 
 * Calculates how well an enterprise buyer matches a startup's pilot offer.
 * 
 * Scoring Weights:
 * - Industry Match: 50% (high weight)
 * - Credibility Score: 30% (medium weight)
 * - Company Size Compatibility: 20% (low weight)
 */
@Service
public class MatchmakingService {

    // Industry compatibility matrix
    private static final Set<String> TECH_INDUSTRIES = Set.of(
        "TECHNOLOGY", "SOFTWARE", "SAAS", "AI/ML", "FINTECH", "HEALTHTECH"
    );
    private static final Set<String> ENTERPRISE_INDUSTRIES = Set.of(
        "FINANCE", "BANKING", "HEALTHCARE", "MANUFACTURING", "RETAIL"
    );

    public MatchScoreResponse calculateMatch(
            Long dealId,
            Long applicationId,
            String enterpriseEmail,
            String startupIndustry,
            String targetIndustry,
            double startupCredibilityScore,
            String enterpriseIndustry,
            String enterpriseCompanySize
    ) {
        List<String> reasons = new ArrayList<>();
        
        // 1. Industry Match (50% weight, max 50 points)
        int industryScore = calculateIndustryScore(
            targetIndustry, enterpriseIndustry, reasons
        );
        
        // 2. Credibility Score (30% weight, max 30 points)
        int credScore = calculateCredibilityScore(
            startupCredibilityScore, reasons
        );
        
        // 3. Company Size Compatibility (20% weight, max 20 points)
        int sizeScore = calculateSizeScore(
            enterpriseCompanySize, startupCredibilityScore, reasons
        );
        
        int totalScore = industryScore + credScore + sizeScore;
        String label = determineLabel(totalScore);
        
        return MatchScoreResponse.builder()
                .dealId(dealId)
                .applicationId(applicationId)
                .enterpriseEmail(enterpriseEmail)
                .matchScore(totalScore)
                .matchLabel(label)
                .industryScore(industryScore)
                .credibilityScore(credScore)
                .companySizeScore(sizeScore)
                .matchReasons(reasons)
                .build();
    }

    private int calculateIndustryScore(
            String targetIndustry, 
            String enterpriseIndustry,
            List<String> reasons
    ) {
        if (targetIndustry == null || enterpriseIndustry == null) {
            reasons.add("Industry information incomplete");
            return 25; // Neutral score
        }
        
        String target = targetIndustry.toUpperCase();
        String enterprise = enterpriseIndustry.toUpperCase();
        
        // Exact match
        if (target.equals(enterprise)) {
            reasons.add("Perfect industry match: " + enterpriseIndustry);
            return 50;
        }
        
        // Same category match
        if ((TECH_INDUSTRIES.contains(target) && TECH_INDUSTRIES.contains(enterprise)) ||
            (ENTERPRISE_INDUSTRIES.contains(target) && ENTERPRISE_INDUSTRIES.contains(enterprise))) {
            reasons.add("Related industry match: " + enterpriseIndustry + " aligns with " + targetIndustry);
            return 40;
        }
        
        // Cross-category (tech targeting enterprise or vice versa)
        if ((TECH_INDUSTRIES.contains(target) && ENTERPRISE_INDUSTRIES.contains(enterprise)) ||
            (ENTERPRISE_INDUSTRIES.contains(target) && TECH_INDUSTRIES.contains(enterprise))) {
            reasons.add("Cross-industry opportunity: " + enterpriseIndustry + " exploring " + targetIndustry);
            return 30;
        }
        
        reasons.add("Industry mismatch: " + enterpriseIndustry + " vs target " + targetIndustry);
        return 15;
    }

    private int calculateCredibilityScore(double credibilityScore, List<String> reasons) {
        if (credibilityScore >= 80) {
            reasons.add("High credibility startup (score: " + credibilityScore + ")");
            return 30;
        } else if (credibilityScore >= 60) {
            reasons.add("Good credibility startup (score: " + credibilityScore + ")");
            return 25;
        } else if (credibilityScore >= 40) {
            reasons.add("Growing credibility startup (score: " + credibilityScore + ")");
            return 20;
        } else if (credibilityScore >= 20) {
            reasons.add("Emerging startup (score: " + credibilityScore + ")");
            return 15;
        } else {
            reasons.add("New startup, limited track record");
            return 10;
        }
    }

    private int calculateSizeScore(
            String companySize, 
            double credibilityScore,
            List<String> reasons
    ) {
        if (companySize == null) {
            return 10;
        }
        
        String size = companySize.toUpperCase();
        
        // Enterprise companies prefer high-credibility startups
        if ("ENTERPRISE".equals(size)) {
            if (credibilityScore >= 60) {
                reasons.add("Enterprise buyer matched with enterprise-ready startup");
                return 20;
            } else {
                reasons.add("Enterprise buyer - startup may need more validation");
                return 10;
            }
        }
        
        // Mid-market is flexible
        if ("MID".equals(size) || "MID_MARKET".equals(size)) {
            reasons.add("Mid-market buyer - good fit for growth-stage startups");
            return 18;
        }
        
        // SMB is open to newer startups
        if ("SMB".equals(size) || "SMALL".equals(size)) {
            reasons.add("SMB buyer - open to innovative solutions");
            return 15;
        }
        
        return 12;
    }

    private String determineLabel(int score) {
        if (score >= 75) return "STRONG_MATCH";
        if (score >= 50) return "GOOD_MATCH";
        return "POOR_MATCH";
    }
}
