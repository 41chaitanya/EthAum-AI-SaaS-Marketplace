package com.ethaum.credibility.repository;

import com.ethaum.credibility.model.CredibilityScore;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CredibilityRepository
        extends JpaRepository<CredibilityScore, Long> {

    Optional<CredibilityScore> findByStartupId(Long startupId);
}
