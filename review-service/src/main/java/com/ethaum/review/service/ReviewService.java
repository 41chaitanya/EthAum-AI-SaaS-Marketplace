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
    private final ReviewVerificationService verificationService;

    public ReviewService(ReviewRepository repo, ReviewVerificationService verificationService) {
        this.repo = repo;
        this.verificationService = verificationService;
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

        // Run AI verification
        VerificationResult verification = verificationService.verify(review);
        review.setVerificationScore(verification.getScore());
        review.setVerificationStatus(verification.getStatus());
        review.setVerifiedAt(LocalDateTime.now());

        return map(repo.save(review));
    }

    public List<ReviewResponse> getReviews(Long launchId) {
        return repo.findByLaunchId(launchId)
                .stream()
                .map(this::map)
                .toList();
    }

    public List<ReviewResponse> getVerifiedReviews(Long launchId) {
        return repo.findByLaunchIdAndVerificationStatus(launchId, "VERIFIED")
                .stream()
                .map(this::map)
                .toList();
    }

    public VerificationResult verifyReview(Long reviewId) {
        Review review = repo.findById(reviewId)
                .orElseThrow(() -> new RuntimeException("Review not found"));
        
        VerificationResult result = verificationService.verify(review);
        
        review.setVerificationScore(result.getScore());
        review.setVerificationStatus(result.getStatus());
        review.setVerifiedAt(LocalDateTime.now());
        repo.save(review);
        
        return result;
    }

    private ReviewResponse map(Review r) {
        return ReviewResponse.builder()
                .id(r.getId())
                .rating(r.getRating())
                .comment(r.getComment())
                .reviewerRole(r.getReviewerRole())
                .companySize(r.getCompanySize())
                .verificationScore(r.getVerificationScore())
                .verificationStatus(r.getVerificationStatus())
                .build();
    }
}
