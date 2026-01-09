package com.ethaum.startup.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class StartupRequest {

    @NotBlank
    private String name;

    private String website;
    private String industry;
    private String fundingStage;
    private String arrRange;
    private String description;
}
