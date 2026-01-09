package com.ethaum.review.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class VerificationResult {

    private int score;              // 0-100
    private String status;          // VERIFIED, NEEDS_REVIEW
    private List<String> flags;     // Issues found
    private String summary;         // Human-readable summary
}
