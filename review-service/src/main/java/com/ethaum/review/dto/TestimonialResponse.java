package com.ethaum.review.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class TestimonialResponse {

    private Long id;
    private String type;
    private String videoUrl;
    private String caseStudyTitle;
    private String caseStudyText;
    private String submitterName;
    private String submitterRole;
    private String companyName;
    private String status;
    private LocalDateTime submittedAt;
}
