package com.ethaum.review.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ReviewResponse {

    private int rating;
    private String comment;
    private String reviewerRole;
    private String companySize;
}
