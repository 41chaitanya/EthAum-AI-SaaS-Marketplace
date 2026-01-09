package com.ethaum.deal.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LeaderboardEntry {

    private int rank;
    private String userEmail;
    private String userRole;
    private int completedReferrals;
    private String badge;
}
