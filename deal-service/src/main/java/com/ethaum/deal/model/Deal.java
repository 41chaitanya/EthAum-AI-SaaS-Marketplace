package com.ethaum.deal.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "deals")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Deal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long startupId;

    private String title;

    @Column(length = 2000)
    private String description;

    private String startupIndustry;

    private String targetIndustry;

    private double startupCredibilityScore;

    private int durationDays;

    private int maxSlots;

    private String status; // OPEN, CLOSED

    private String ownerEmail;

    private LocalDateTime createdAt;
}
