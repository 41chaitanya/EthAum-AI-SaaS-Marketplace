package com.ethaum.startup.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class StartupResponse {

    private Long id;
    private String name;
    private String website;
    private String industry;
    private String fundingStage;
    private String arrRange;
    private String description;
}
