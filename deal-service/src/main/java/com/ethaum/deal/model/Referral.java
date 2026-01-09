package com.ethaum.deal.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "referrals")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Referral {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Who referred
    private String referrerEmail;
    private String referrerRole; // STARTUP, ENTERPRISE

    // Unique referral code
    private String referralCode;

    // Who was referred
    private String referredEmail;
    private String referredRole;

    // Status: PENDING, COMPLETED
    private String status;

    private LocalDateTime createdAt;
    private LocalDateTime completedAt;
}
