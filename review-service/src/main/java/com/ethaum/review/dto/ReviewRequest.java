package com.ethaum.review.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class ReviewRequest {

    private Long launchId;

    @Min(1)
    @Max(5)
    private int rating;

    private String reviewerRole; // CTO, CXO, MANAGER

    private String companySize; // SMB, MID, ENTERPRISE

    @NotBlank
    private String comment;
}
