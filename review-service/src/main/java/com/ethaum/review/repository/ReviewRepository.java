package com.ethaum.review.repository;

import com.ethaum.review.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    List<Review> findByLaunchId(Long launchId);
}
