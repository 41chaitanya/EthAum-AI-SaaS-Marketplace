package com.ethaum.review.service;

import com.ethaum.review.dto.*;
import com.ethaum.review.model.Review;
import com.ethaum.review.repository.ReviewRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReviewService {

    private final ReviewRepository repo;

    public ReviewService(ReviewRepository repo) {
        this.repo = repo;
    }

    public ReviewResponse addReview(
            ReviewRequest req,
            String reviewerEmail
    ) {
        Review review = Review.builder()
                .launchId(req.getLaunchId())
                .rating(req.getRating())
                .comment(req.getComment())
                .reviewerRole(req.getReviewerRole())
                .companySize(req.getCompanySize())
                .reviewerEmail(reviewerEmail)
                .createdAt(LocalDateTime.now())
                .build();

        return map(repo.save(review));
    }

    public List<ReviewResponse> getReviews(Long launchId) {
        return repo.findByLaunchId(launchId)
                .stream()
                .map(this::map)
                .toList();
    }

    private ReviewResponse map(Review r) {
        return ReviewResponse.builder()
                .rating(r.getRating())
                .comment(r.getComment())
                .reviewerRole(r.getReviewerRole())
                .companySize(r.getCompanySize())
                .build();
    }
}
