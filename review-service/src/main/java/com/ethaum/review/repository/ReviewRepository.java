package com.ethaum.review.repository;

import com.ethaum.review.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    List<Review> findByLaunchId(Long launchId);

    List<Review> findByLaunchIdAndVerificationStatus(Long launchId, String status);

    @Query("SELECT AVG(r.rating) FROM Review r WHERE r.launchId = :launchId")
    Double getAverageRating(Long launchId);

    long countByLaunchId(Long launchId);

    long countByLaunchIdAndCompanySize(Long launchId, String companySize);
}
