package com.ethaum.launch.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class TemplateResponse {
    private String suggestedTagline;
    private String suggestedDescription;
    private String valueProposition;
    private List<String> alternativeTaglines;
}
