package com.ethaum.credibility.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "credibility_scores")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CredibilityScore {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long startupId;

    private double score; // 0–100

    private String level; // Emerging, Growing, Enterprise Ready

    private String badge; // EthAum Verified, Enterprise Ready

    private LocalDateTime calculatedAt;
}
