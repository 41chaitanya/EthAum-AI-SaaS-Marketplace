package com.ethaum.launch.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ViralityResponse {
    private Long launchId;
    private double viralityScore;
    private boolean trending;
    private int upvotes;
    private int commentsCount;
    private int viewCount;
    private String badge;
}
