package com.ethaum.deal.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "referral_stats")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReferralStats {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String userEmail;
    private String userRole;

    // Unique referral code for this user
    private String referralCode;

    // Counts
    private int totalReferrals;
    private int completedReferrals;
    private int pendingReferrals;

    // Badges earned
    private String badge; // TOP_REFERRER, GROWTH_CHAMPION, etc.

    private LocalDateTime lastReferralAt;
    private LocalDateTime badgeEarnedAt;
}
