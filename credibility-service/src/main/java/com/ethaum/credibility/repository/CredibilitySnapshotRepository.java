package com.ethaum.credibility.repository;

import com.ethaum.credibility.model.CredibilitySnapshot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface CredibilitySnapshotRepository extends JpaRepository<CredibilitySnapshot, Long> {

    List<CredibilitySnapshot> findByStartupIdAndSnapshotDateBetweenOrderBySnapshotDateAsc(
            Long startupId, LocalDate startDate, LocalDate endDate);

    Optional<CredibilitySnapshot> findByStartupIdAndSnapshotDate(Long startupId, LocalDate date);

    @Query("SELECT cs FROM CredibilitySnapshot cs WHERE cs.startupId = :startupId ORDER BY cs.snapshotDate DESC")
    List<CredibilitySnapshot> findLatestByStartupId(Long startupId);

    List<CredibilitySnapshot> findByStartupIdOrderBySnapshotDateDesc(Long startupId);
}
