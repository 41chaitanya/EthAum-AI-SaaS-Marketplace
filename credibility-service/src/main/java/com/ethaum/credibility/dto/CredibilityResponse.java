package com.ethaum.credibility.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CredibilityResponse {

    private Long startupId;
    private double score;
    private String level;
    private String badge;
}
