package com.ethaum.deal.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "deal_applications")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DealApplication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long dealId;

    private String enterpriseEmail;

    private String enterpriseIndustry;

    private String enterpriseCompanySize;

    private String status; // APPLIED, ACCEPTED, REJECTED

    // AI Matchmaking
    private int matchScore;

    private String matchLabel;

    private LocalDateTime appliedAt;
}
