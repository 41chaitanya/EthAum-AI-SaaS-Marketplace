package com.ethaum.deal.dto;

import lombok.Data;

@Data
public class DealRequest {

    private Long startupId;
    private String title;
    private String description;
    private String startupIndustry;
    private String targetIndustry;
    private double startupCredibilityScore;
    private int durationDays;
    private int maxSlots;
}
