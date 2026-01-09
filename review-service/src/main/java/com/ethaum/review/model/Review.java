package com.ethaum.review.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "reviews")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long launchId;

    private String reviewerEmail;

    private String reviewerRole; // CTO, CXO, MANAGER

    private String companySize; // SMB, MID, ENTERPRISE

    private int rating; // 1–5

    @Column(length = 2000)
    private String comment;

    private LocalDateTime createdAt;
}
