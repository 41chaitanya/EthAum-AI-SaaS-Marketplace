package com.ethaum.deal.controller;

import com.ethaum.deal.dto.*;
import com.ethaum.deal.security.JwtHeaderUtil;
import com.ethaum.deal.service.ReferralService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/referrals")
public class ReferralController {

    private final ReferralService service;
    private final JwtHeaderUtil jwtUtil;

    public ReferralController(ReferralService service, JwtHeaderUtil jwtUtil) {
        this.service = service;
        this.jwtUtil = jwtUtil;
    }

    // Get or create referral code for logged-in user
    @GetMapping("/my-code")
    public ReferralStatsResponse getMyReferralCode(
            @RequestHeader("Authorization") String auth
    ) {
        String token = auth.replace("Bearer ", "");
        String email = jwtUtil.extractEmail(token);
        String role = jwtUtil.extractRole(token);
        return service.getOrCreateReferralCode(email, role);
    }

    // Get my referral stats
    @GetMapping("/my-stats")
    public ReferralStatsResponse getMyStats(
            @RequestHeader("Authorization") String auth
    ) {
        String token = auth.replace("Bearer ", "");
        String email = jwtUtil.extractEmail(token);
        return service.getStats(email);
    }

    // Track a referral (called during signup)
    @PostMapping("/track")
    public String trackReferral(
            @RequestParam String referralCode,
            @RequestParam String referredEmail,
            @RequestParam(defaultValue = "ENTERPRISE") String referredRole
    ) {
        return service.trackReferral(referralCode, referredEmail, referredRole);
    }

    // Complete a referral (called when referred user takes action)
    @PostMapping("/complete")
    public String completeReferral(@RequestParam String referredEmail) {
        return service.completeReferral(referredEmail);
    }

    // Public leaderboard
    @GetMapping("/leaderboard")
    public List<LeaderboardEntry> getLeaderboard(
            @RequestParam(defaultValue = "10") int limit
    ) {
        return service.getLeaderboard(limit);
    }
}
