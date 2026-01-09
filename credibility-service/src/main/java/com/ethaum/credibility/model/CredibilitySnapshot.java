package com.ethaum.credibility.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

/**
 * Stores daily snapshots of credibility metrics for trend analysis
 */
@Entity
@Table(name = "credibility_snapshots")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CredibilitySnapshot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long startupId;

    private LocalDate snapshotDate;

    private double credibilityScore;

    private int totalUpvotes;

    private int totalReviews;

    private double avgRating;

    private int enterpriseReviews;

    private double viralityScore;

    private int launchCount;
}
