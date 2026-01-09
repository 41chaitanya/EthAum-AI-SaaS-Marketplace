package com.ethaum.launch.repository;

import com.ethaum.launch.model.Launch;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LaunchRepository extends JpaRepository<Launch, Long> {

    List<Launch> findByCategory(String category);

    List<Launch> findAllByOrderByUpvotesDesc();

    List<Launch> findByTrendingTrueOrderByViralityScoreDesc();

    List<Launch> findByBadgeOrderByViralityScoreDesc(String badge);

    List<Launch> findAllByOrderByViralityScoreDesc();
}
