package com.ethaum.deal.repository;

import com.ethaum.deal.model.Referral;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReferralRepository extends JpaRepository<Referral, Long> {

    List<Referral> findByReferrerEmail(String referrerEmail);

    Optional<Referral> findByReferralCodeAndReferredEmail(String code, String referredEmail);

    Optional<Referral> findByReferredEmail(String referredEmail);

    long countByReferrerEmailAndStatus(String referrerEmail, String status);
}
