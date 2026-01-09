package com.ethaum.review.repository;

import com.ethaum.review.model.Testimonial;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TestimonialRepository extends JpaRepository<Testimonial, Long> {

    List<Testimonial> findByStartupIdAndStatus(Long startupId, String status);

    List<Testimonial> findByLaunchIdAndStatus(Long launchId, String status);

    List<Testimonial> findByStatus(String status);

    List<Testimonial> findByStartupId(Long startupId);

    long countByStartupIdAndStatus(Long startupId, String status);
}
