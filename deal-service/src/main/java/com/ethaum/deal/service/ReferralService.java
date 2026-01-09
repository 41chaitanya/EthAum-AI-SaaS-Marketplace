package com.ethaum.deal.service;

import com.ethaum.deal.dto.*;
import com.ethaum.deal.model.*;
import com.ethaum.deal.repository.*;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class ReferralService {

    private final ReferralRepository referralRepo;
    private final ReferralStatsRepository statsRepo;

    private static final int STARTER_THRESHOLD = 1;
    private static final int GROWTH_CHAMPION_THRESHOLD = 5;
    private static final int TOP_REFERRER_THRESHOLD = 10;
    private static final int VIRAL_LEGEND_THRESHOLD = 25;

    public ReferralService(ReferralRepository referralRepo, ReferralStatsRepository statsRepo) {
        this.referralRepo = referralRepo;
        this.statsRepo = statsRepo;
    }

    public ReferralStatsResponse getOrCreateReferralCode(String userEmail, String userRole) {
        ReferralStats stats = statsRepo.findByUserEmail(userEmail)
                .orElseGet(() -> createNewStats(userEmail, userRole));
        return buildStatsResponse(stats);
    }

    public String trackReferral(String referralCode, String referredEmail, String referredRole) {
        ReferralStats referrerStats = statsRepo.findByReferralCode(referralCode)
                .orElseThrow(() -> new RuntimeException("Invalid referral code"));

        if (referralRepo.findByReferredEmail(referredEmail).isPresent()) {
            return "User already referred";
        }

        Referral referral = Referral.builder()
                .referrerEmail(referrerStats.getUserEmail())
                .referrerRole(referrerStats.getUserRole())
                .referralCode(referralCode)
                .referredEmail(referredEmail)
                .referredRole(referredRole)
                .status("PENDING")
                .createdAt(LocalDateTime.now())
                .build();

        referralRepo.save(referral);

        referrerStats.setTotalReferrals(referrerStats.getTotalReferrals() + 1);
        referrerStats.setPendingReferrals(referrerStats.getPendingReferrals() + 1);
        referrerStats.setLastReferralAt(LocalDateTime.now());
        statsRepo.save(referrerStats);

        return "Referral tracked successfully";
    }

    public String completeReferral(String referredEmail) {
        Referral referral = referralRepo.findByReferredEmail(referredEmail).orElse(null);

        if (referral == null || "COMPLETED".equals(referral.getStatus())) {
            return "No pending referral found";
        }

        referral.setStatus("COMPLETED");
        referral.setCompletedAt(LocalDateTime.now());
        referralRepo.save(referral);

        ReferralStats stats = statsRepo.findByUserEmail(referral.getReferrerEmail()).orElseThrow();
        stats.setCompletedReferrals(stats.getCompletedReferrals() + 1);
        stats.setPendingReferrals(Math.max(0, stats.getPendingReferrals() - 1));
        updateBadge(stats);
        statsRepo.save(stats);

        return "Referral completed! " + referral.getReferrerEmail() + " earned credit.";
    }

    public List<LeaderboardEntry> getLeaderboard(int limit) {
        List<ReferralStats> topReferrers = statsRepo.findTopReferrers();
        List<LeaderboardEntry> leaderboard = new ArrayList<>();

        int rank = 1;
        for (ReferralStats stats : topReferrers) {
            if (rank > limit) break;
            if (stats.getCompletedReferrals() == 0) continue;

            leaderboard.add(LeaderboardEntry.builder()
                    .rank(rank++)
                    .userEmail(maskEmail(stats.getUserEmail()))
                    .userRole(stats.getUserRole())
                    .completedReferrals(stats.getCompletedReferrals())
                    .badge(stats.getBadge())
                    .build());
        }
        return leaderboard;
    }

    public ReferralStatsResponse getStats(String userEmail) {
        ReferralStats stats = statsRepo.findByUserEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("No referral stats found"));
        return buildStatsResponse(stats);
    }

    private ReferralStats createNewStats(String userEmail, String userRole) {
        String code = generateReferralCode(userEmail);
        ReferralStats stats = ReferralStats.builder()
                .userEmail(userEmail)
                .userRole(userRole)
                .referralCode(code)
                .totalReferrals(0)
                .completedReferrals(0)
                .pendingReferrals(0)
                .build();
        return statsRepo.save(stats);
    }

    private String generateReferralCode(String email) {
        String prefix = email.split("@")[0].toUpperCase();
        if (prefix.length() > 4) prefix = prefix.substring(0, 4);
        String suffix = UUID.randomUUID().toString().substring(0, 4).toUpperCase();
        return prefix + "-" + suffix;
    }

    private void updateBadge(ReferralStats stats) {
        int completed = stats.getCompletedReferrals();
        String newBadge = null;

        if (completed >= VIRAL_LEGEND_THRESHOLD) {
            newBadge = "VIRAL_LEGEND";
        } else if (completed >= TOP_REFERRER_THRESHOLD) {
            newBadge = "TOP_REFERRER";
        } else if (completed >= GROWTH_CHAMPION_THRESHOLD) {
            newBadge = "GROWTH_CHAMPION";
        } else if (completed >= STARTER_THRESHOLD) {
            newBadge = "REFERRAL_STARTER";
        }

        if (newBadge != null && !newBadge.equals(stats.getBadge())) {
            stats.setBadge(newBadge);
            stats.setBadgeEarnedAt(LocalDateTime.now());
        }
    }

    private ReferralStatsResponse buildStatsResponse(ReferralStats stats) {
        String nextBadge = getNextBadge(stats.getCompletedReferrals());
        int toNext = getReferralsToNextBadge(stats.getCompletedReferrals());

        return ReferralStatsResponse.builder()
                .userEmail(stats.getUserEmail())
                .referralCode(stats.getReferralCode())
                .referralLink("https://ethaum.ai/signup?ref=" + stats.getReferralCode())
                .totalReferrals(stats.getTotalReferrals())
                .completedReferrals(stats.getCompletedReferrals())
                .pendingReferrals(stats.getPendingReferrals())
                .badge(stats.getBadge())
                .nextBadge(nextBadge)
                .referralsToNextBadge(toNext)
                .build();
    }

    private String getNextBadge(int completed) {
        if (completed < STARTER_THRESHOLD) return "REFERRAL_STARTER";
        if (completed < GROWTH_CHAMPION_THRESHOLD) return "GROWTH_CHAMPION";
        if (completed < TOP_REFERRER_THRESHOLD) return "TOP_REFERRER";
        if (completed < VIRAL_LEGEND_THRESHOLD) return "VIRAL_LEGEND";
        return null;
    }

    private int getReferralsToNextBadge(int completed) {
        if (completed < STARTER_THRESHOLD) return STARTER_THRESHOLD - completed;
        if (completed < GROWTH_CHAMPION_THRESHOLD) return GROWTH_CHAMPION_THRESHOLD - completed;
        if (completed < TOP_REFERRER_THRESHOLD) return TOP_REFERRER_THRESHOLD - completed;
        if (completed < VIRAL_LEGEND_THRESHOLD) return VIRAL_LEGEND_THRESHOLD - completed;
        return 0;
    }

    private String maskEmail(String email) {
        String[] parts = email.split("@");
        if (parts[0].length() <= 2) return email;
        return parts[0].substring(0, 2) + "***@" + parts[1];
    }
}
