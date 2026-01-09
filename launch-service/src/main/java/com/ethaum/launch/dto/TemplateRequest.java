package com.ethaum.launch.dto;

import lombok.Data;

@Data
public class TemplateRequest {
    private String productName;
    private String category;    // AI/ML, FinTech, HealthTech, SaaS
    private String industry;    // B2B, B2C, Enterprise
    private String problemSolved; // Optional - one liner
}
