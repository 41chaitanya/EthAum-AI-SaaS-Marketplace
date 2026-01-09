package com.ethaum.review.dto;

import lombok.Data;

@Data
public class TestimonialRequest {

    private Long startupId;
    private Long launchId;

    // VIDEO or CASE_STUDY
    private String type;

    // For video testimonials
    private String videoUrl;

    // For case study
    private String caseStudyTitle;
    private String caseStudyText;

    // Submitter details
    private String submitterName;
    private String submitterRole;
    private String companyName;
}
