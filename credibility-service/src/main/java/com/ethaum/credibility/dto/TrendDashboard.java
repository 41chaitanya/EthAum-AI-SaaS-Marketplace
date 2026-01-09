package com.ethaum.credibility.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class TrendDashboard {

    private Long startupId;
    private String granularity; // WEEKLY, MONTHLY
    
    // Time series data
    private List<TrendDataPoint> dataPoints;
    
    // Summary metrics
    private double currentScore;
    private double scoreChange; // % change from first to last
    private String trend; // RISING, STABLE, DECLINING
    
    // Insights
    private String insight;
}
