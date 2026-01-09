package com.ethaum.launch.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LaunchResponse {

    private Long id;
    private String productName;
    private String tagline;
    private String category;
    private int upvotes;
}
