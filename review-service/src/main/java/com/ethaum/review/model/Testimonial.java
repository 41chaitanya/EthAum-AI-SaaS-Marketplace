package com.ethaum.review.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "testimonials")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Testimonial {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long startupId;

    private Long launchId;

    // VIDEO or CASE_STUDY
    private String type;

    // YouTube / Loom URL (for video)
    private String videoUrl;

    // Case study text
    @Column(length = 5000)
    private String caseStudyText;

    private String caseStudyTitle;

    // Submitter info
    private String submitterEmail;
    private String submitterName;
    private String submitterRole;
    private String companyName;

    // Moderation: PENDING, APPROVED, REJECTED
    private String status;

    private LocalDateTime submittedAt;
    private LocalDateTime moderatedAt;
    private String moderatedBy;
}
