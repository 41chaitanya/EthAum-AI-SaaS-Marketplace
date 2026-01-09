package com.ethaum.credibility.service;

import com.ethaum.credibility.dto.*;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Generates Validation Reports for startups
 * Replaces expensive analyst reports with automated insights
 */
@Service
public class ValidationReportService {

    private final QuadrantService quadrantService;

    public ValidationReportService(QuadrantService quadrantService) {
        this.quadrantService = quadrantService;
    }

    public ValidationReport generateReport(
            Long startupId,
            String startupName,
            double credibilityScore,
            String credibilityLevel,
            String badge,
            int totalUpvotes,
            int launchCount,
            double viralityScore,
            double avgRating,
            int totalReviews,
            int enterpriseReviews,
            int verifiedReviews,
            int videoTestimonials,
            int caseStudies,
            String reportType
    ) {
        // Calculate quadrant position
        QuadrantPosition quadrant = quadrantService.calculatePosition(
                startupId, startupName, totalUpvotes, launchCount,
                viralityScore, avgRating, totalReviews, enterpriseReviews
        );

        // Build trust signals
        TrustSignals trustSignals = TrustSignals.builder()
                .totalReviews(totalReviews)
                .averageRating(avgRating)
                .enterpriseReviews(enterpriseReviews)
                .verifiedReviews(verifiedReviews)
                .totalLaunches(launchCount)
                .totalUpvotes(totalUpvotes)
                .avgViralityScore(viralityScore)
                .videoTestimonials(videoTestimonials)
                .caseStudies(caseStudies)
                .hasEnterpriseBadge("Enterprise Ready".equals(badge))
                .hasFeaturedLaunch(viralityScore >= 75)
                .build();

        // Generate highlights
        List<String> highlights = generateHighlights(
                credibilityScore, quadrant, trustSignals, reportType
        );

        // Generate recommendation
        String recommendation = generateRecommendation(
                quadrant.getQuadrant(), credibilityLevel, reportType
        );

        return ValidationReport.builder()
                .startupId(startupId)
                .startupName(startupName)
                .generatedAt(LocalDateTime.now())
                .credibilityScore(credibilityScore)
                .credibilityLevel(credibilityLevel)
                .badge(badge)
                .quadrantPosition(quadrant)
                .trustSignals(trustSignals)
                .highlights(highlights)
                .recommendation(recommendation)
                .reportId(UUID.randomUUID().toString().substring(0, 8).toUpperCase())
                .reportType(reportType)
                .build();
    }

    private List<String> generateHighlights(
            double score, 
            QuadrantPosition quadrant,
            TrustSignals signals,
            String reportType
    ) {
        List<String> highlights = new ArrayList<>();

        // Score-based highlights
        if (score >= 80) {
            highlights.add("Top-tier credibility score of " + score + "/100");
        } else if (score >= 60) {
            highlights.add("Strong credibility score of " + score + "/100");
        }

        // Quadrant highlights
        if ("LEADER".equals(quadrant.getQuadrant())) {
            highlights.add("Positioned as Market Leader in EthAum Quadrant");
        } else if ("VISIONARY".equals(quadrant.getQuadrant())) {
            highlights.add("Recognized as Visionary with strong enterprise trust");
        }

        // Trust signal highlights
        if (signals.getEnterpriseReviews() >= 5) {
            highlights.add(signals.getEnterpriseReviews() + " enterprise customer reviews");
        }

        if (signals.getAverageRating() >= 4.5) {
            highlights.add("Exceptional " + signals.getAverageRating() + "/5 average rating");
        }

        if (signals.getVideoTestimonials() > 0) {
            highlights.add(signals.getVideoTestimonials() + " video testimonial(s) from customers");
        }

        if (signals.getCaseStudies() > 0) {
            highlights.add(signals.getCaseStudies() + " documented case study/studies");
        }

        if (signals.isHasEnterpriseBadge()) {
            highlights.add("Earned 'Enterprise Ready' badge");
        }

        if (signals.isHasFeaturedLaunch()) {
            highlights.add("Featured launch with high virality");
        }

        // Report type specific
        if ("INVESTOR".equals(reportType)) {
            if (signals.getTotalUpvotes() >= 100) {
                highlights.add("Strong market validation with " + signals.getTotalUpvotes() + " upvotes");
            }
        } else if ("ENTERPRISE".equals(reportType)) {
            if (signals.getVerifiedReviews() >= 10) {
                highlights.add(signals.getVerifiedReviews() + " AI-verified authentic reviews");
            }
        }

        return highlights;
    }

    private String generateRecommendation(String quadrant, String level, String reportType) {
        String base = switch (quadrant) {
            case "LEADER" -> "This startup demonstrates strong market traction and enterprise trust. ";
            case "CHALLENGER" -> "This startup shows promising market activity. Enterprise validation recommended. ";
            case "VISIONARY" -> "Trusted by enterprises but needs broader market exposure. ";
            case "NICHE_PLAYER" -> "Early-stage startup with growth potential. Due diligence recommended. ";
            default -> "";
        };

        String action = switch (reportType) {
            case "INVESTOR" -> switch (level) {
                case "Enterprise Ready" -> "Strong investment candidate for growth-stage funding.";
                case "Growing" -> "Consider for early-stage investment with milestone-based approach.";
                default -> "Monitor progress before investment decision.";
            };
            case "ENTERPRISE" -> switch (level) {
                case "Enterprise Ready" -> "Suitable for enterprise procurement and pilot programs.";
                case "Growing" -> "Consider for departmental pilots with defined success criteria.";
                default -> "Recommend waiting for more enterprise validation.";
            };
            default -> "Review detailed metrics for decision making.";
        };

        return base + action;
    }
}
