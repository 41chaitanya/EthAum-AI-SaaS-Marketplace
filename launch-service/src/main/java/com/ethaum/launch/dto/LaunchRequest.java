package com.ethaum.launch.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LaunchRequest {

    private Long startupId;

    @NotBlank
    private String productName;

    private String tagline;

    private String description;

    private String category;
}
