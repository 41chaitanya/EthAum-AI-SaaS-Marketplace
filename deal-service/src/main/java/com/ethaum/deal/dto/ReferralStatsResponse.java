package com.ethaum.deal.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ReferralStatsResponse {

    private String userEmail;
    private String referralCode;
    private String referralLink;
    
    private int totalReferrals;
    private int completedReferrals;
    private int pendingReferrals;
    
    private String badge;
    private String nextBadge;
    private int referralsToNextBadge;
}
