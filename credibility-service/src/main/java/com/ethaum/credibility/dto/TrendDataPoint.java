package com.ethaum.credibility.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class TrendDataPoint {

    private LocalDate date;
    private double credibilityScore;
    private int upvotes;
    private int reviews;
    private double viralityScore;
}
