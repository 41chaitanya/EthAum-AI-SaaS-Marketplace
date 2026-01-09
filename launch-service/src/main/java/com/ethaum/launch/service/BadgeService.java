package com.ethaum.launch.service;

import com.ethaum.launch.model.Launch;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class BadgeService {

    // Thresholds
    private static final double RISING_STAR_THRESHOLD = 25.0;
    private static final double TRENDING_THRESHOLD = 50.0;
    private static final double FEATURED_THRESHOLD = 75.0;
    private static final double TOP_LAUNCH_THRESHOLD = 90.0;

    public enum LaunchBadge {
        NONE(""),
        ETHAUM_VERIFIED("EthAum Verified"),
        RISING_STAR("Rising Star"),
        TRENDING("Trending on EthAum"),
        FEATURED("Featured Launch"),
        TOP_LAUNCH("Top Launch");

        private final String displayName;

        LaunchBadge(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }

    public String determineBadge(Launch launch) {
        double score = launch.getViralityScore();

        if (score >= TOP_LAUNCH_THRESHOLD) {
            return LaunchBadge.TOP_LAUNCH.getDisplayName();
        } else if (score >= FEATURED_THRESHOLD) {
            return LaunchBadge.FEATURED.getDisplayName();
        } else if (score >= TRENDING_THRESHOLD) {
            return LaunchBadge.TRENDING.getDisplayName();
        } else if (score >= RISING_STAR_THRESHOLD) {
            return LaunchBadge.RISING_STAR.getDisplayName();
        } else if (launch.getId() != null) {
            return LaunchBadge.ETHAUM_VERIFIED.getDisplayName();
        }
        return LaunchBadge.NONE.getDisplayName();
    }

    public void updateBadge(Launch launch) {
        String newBadge = determineBadge(launch);
        if (!newBadge.equals(launch.getBadge())) {
            launch.setBadge(newBadge);
            launch.setBadgeAssignedAt(LocalDateTime.now());
        }
    }
}
