package com.ethaum.deal.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ApplicationResponse {

    private Long applicationId;
    private Long dealId;
    private String enterpriseEmail;
    private String status;
    
    // AI Matchmaking
    private int matchScore;
    private String matchLabel;
    private List<String> matchReasons;
}
