package com.ethaum.deal.repository;

import com.ethaum.deal.model.ReferralStats;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ReferralStatsRepository extends JpaRepository<ReferralStats, Long> {

    Optional<ReferralStats> findByUserEmail(String userEmail);

    Optional<ReferralStats> findByReferralCode(String referralCode);

    @Query("SELECT rs FROM ReferralStats rs ORDER BY rs.completedReferrals DESC")
    List<ReferralStats> findTopReferrers();

    List<ReferralStats> findByBadgeIsNotNullOrderByCompletedReferralsDesc();
}
