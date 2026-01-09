package com.ethaum.credibility.controller;

import com.ethaum.credibility.dto.*;
import com.ethaum.credibility.service.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/insights")
public class InsightsController {

    private final QuadrantService quadrantService;
    private final TrendService trendService;
    private final ValidationReportService reportService;

    public InsightsController(
            QuadrantService quadrantService,
            TrendService trendService,
            ValidationReportService reportService
    ) {
        this.quadrantService = quadrantService;
        this.trendService = trendService;
        this.reportService = reportService;
    }

    // ========== QUADRANT ==========

    @GetMapping("/quadrant/{startupId}")
    public QuadrantPosition getQuadrantPosition(
            @PathVariable Long startupId,
            @RequestParam(defaultValue = "Startup") String startupName,
            @RequestParam(defaultValue = "0") int totalUpvotes,
            @RequestParam(defaultValue = "1") int launchCount,
            @RequestParam(defaultValue = "0") double viralityScore,
            @RequestParam(defaultValue = "0") double avgRating,
            @RequestParam(defaultValue = "0") int totalReviews,
            @RequestParam(defaultValue = "0") int enterpriseReviews
    ) {
        return quadrantService.calculatePosition(
                startupId, startupName, totalUpvotes, launchCount,
                viralityScore, avgRating, totalReviews, enterpriseReviews
        );
    }

    // ========== TRENDS ==========

    @GetMapping("/trends/{startupId}")
    public TrendDashboard getTrends(
            @PathVariable Long startupId,
            @RequestParam(defaultValue = "WEEKLY") String granularity
    ) {
        return trendService.getTrend(startupId, granularity);
    }

    @PostMapping("/trends/{startupId}/snapshot")
    public String recordSnapshot(
            @PathVariable Long startupId,
            @RequestParam double credibilityScore,
            @RequestParam(defaultValue = "0") int upvotes,
            @RequestParam(defaultValue = "0") int reviews,
            @RequestParam(defaultValue = "0") double avgRating,
            @RequestParam(defaultValue = "0") int enterpriseReviews,
            @RequestParam(defaultValue = "0") double viralityScore,
            @RequestParam(defaultValue = "1") int launchCount
    ) {
        trendService.recordSnapshot(
                startupId, credibilityScore, upvotes, reviews,
                avgRating, enterpriseReviews, viralityScore, launchCount
        );
        return "Snapshot recorded";
    }

    // ========== VALIDATION REPORTS ==========

    @GetMapping("/report/{startupId}")
    public ValidationReport generateReport(
            @PathVariable Long startupId,
            @RequestParam(defaultValue = "Startup") String startupName,
            @RequestParam double credibilityScore,
            @RequestParam(defaultValue = "Emerging") String credibilityLevel,
            @RequestParam(defaultValue = "EthAum Verified") String badge,
            @RequestParam(defaultValue = "0") int totalUpvotes,
            @RequestParam(defaultValue = "1") int launchCount,
            @RequestParam(defaultValue = "0") double viralityScore,
            @RequestParam(defaultValue = "0") double avgRating,
            @RequestParam(defaultValue = "0") int totalReviews,
            @RequestParam(defaultValue = "0") int enterpriseReviews,
            @RequestParam(defaultValue = "0") int verifiedReviews,
            @RequestParam(defaultValue = "0") int videoTestimonials,
            @RequestParam(defaultValue = "0") int caseStudies,
            @RequestParam(defaultValue = "GENERAL") String reportType
    ) {
        return reportService.generateReport(
                startupId, startupName, credibilityScore, credibilityLevel, badge,
                totalUpvotes, launchCount, viralityScore, avgRating,
                totalReviews, enterpriseReviews, verifiedReviews,
                videoTestimonials, caseStudies, reportType
        );
    }

    // Investor-focused report
    @GetMapping("/report/{startupId}/investor")
    public ValidationReport investorReport(
            @PathVariable Long startupId,
            @RequestParam(defaultValue = "Startup") String startupName,
            @RequestParam double credibilityScore,
            @RequestParam(defaultValue = "Emerging") String credibilityLevel,
            @RequestParam(defaultValue = "EthAum Verified") String badge,
            @RequestParam(defaultValue = "0") int totalUpvotes,
            @RequestParam(defaultValue = "1") int launchCount,
            @RequestParam(defaultValue = "0") double viralityScore,
            @RequestParam(defaultValue = "0") double avgRating,
            @RequestParam(defaultValue = "0") int totalReviews,
            @RequestParam(defaultValue = "0") int enterpriseReviews,
            @RequestParam(defaultValue = "0") int verifiedReviews,
            @RequestParam(defaultValue = "0") int videoTestimonials,
            @RequestParam(defaultValue = "0") int caseStudies
    ) {
        return reportService.generateReport(
                startupId, startupName, credibilityScore, credibilityLevel, badge,
                totalUpvotes, launchCount, viralityScore, avgRating,
                totalReviews, enterpriseReviews, verifiedReviews,
                videoTestimonials, caseStudies, "INVESTOR"
        );
    }

    // Enterprise-focused report
    @GetMapping("/report/{startupId}/enterprise")
    public ValidationReport enterpriseReport(
            @PathVariable Long startupId,
            @RequestParam(defaultValue = "Startup") String startupName,
            @RequestParam double credibilityScore,
            @RequestParam(defaultValue = "Emerging") String credibilityLevel,
            @RequestParam(defaultValue = "EthAum Verified") String badge,
            @RequestParam(defaultValue = "0") int totalUpvotes,
            @RequestParam(defaultValue = "1") int launchCount,
            @RequestParam(defaultValue = "0") double viralityScore,
            @RequestParam(defaultValue = "0") double avgRating,
            @RequestParam(defaultValue = "0") int totalReviews,
            @RequestParam(defaultValue = "0") int enterpriseReviews,
            @RequestParam(defaultValue = "0") int verifiedReviews,
            @RequestParam(defaultValue = "0") int videoTestimonials,
            @RequestParam(defaultValue = "0") int caseStudies
    ) {
        return reportService.generateReport(
                startupId, startupName, credibilityScore, credibilityLevel, badge,
                totalUpvotes, launchCount, viralityScore, avgRating,
                totalReviews, enterpriseReviews, verifiedReviews,
                videoTestimonials, caseStudies, "ENTERPRISE"
        );
    }
}
