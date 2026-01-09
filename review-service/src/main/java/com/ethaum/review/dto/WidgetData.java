package com.ethaum.review.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class WidgetData {

    private Long launchId;
    private double averageRating;
    private int reviewCount;
    private int enterpriseReviewCount;
    private String badge;
    private String badgeColor;
}
