package com.ethaum.review.service;

import com.ethaum.review.dto.VerificationResult;
import com.ethaum.review.model.Review;
import com.ethaum.review.repository.ReviewRepository;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Rule-based AI Review Verification
 * 
 * MVP approach: Simple heuristics that can later be replaced with LLM
 * 
 * Scoring:
 * - Base score: 100
 * - Deductions for each issue found
 * - Final: VERIFIED (>=70) or NEEDS_REVIEW (<70)
 */
@Service
public class ReviewVerificationService {

    private final ReviewRepository reviewRepo;

    // Generic phrases that indicate low-quality reviews
    private static final Set<String> GENERIC_PHRASES = Set.of(
        "great product", "highly recommend", "best ever",
        "amazing service", "love it", "must have",
        "five stars", "10/10", "perfect",
        "worst product", "terrible", "hate it",
        "do not buy", "waste of money"
    );

    // Minimum review length for quality
    private static final int MIN_LENGTH = 50;
    private static final int IDEAL_LENGTH = 150;

    public ReviewVerificationService(ReviewRepository reviewRepo) {
        this.reviewRepo = reviewRepo;
    }

    public VerificationResult verify(Review review) {
        int score = 100;
        List<String> flags = new ArrayList<>();

        // 1. Length Check (-20 if too short)
        int length = review.getComment().length();
        if (length < MIN_LENGTH) {
            score -= 20;
            flags.add("Review too short (" + length + " chars, min: " + MIN_LENGTH + ")");
        } else if (length >= IDEAL_LENGTH) {
            score += 5; // Bonus for detailed review
        }

        // 2. Generic Language Detection (-15 per generic phrase)
        String lowerComment = review.getComment().toLowerCase();
        int genericCount = 0;
        for (String phrase : GENERIC_PHRASES) {
            if (lowerComment.contains(phrase)) {
                genericCount++;
            }
        }
        if (genericCount > 0) {
            int deduction = Math.min(genericCount * 15, 30);
            score -= deduction;
            flags.add("Contains " + genericCount + " generic phrase(s)");
        }

        // 3. Duplicate Detection (-30 if duplicate found)
        if (isDuplicate(review)) {
            score -= 30;
            flags.add("Possible duplicate content detected");
        }

        // 4. Spam Indicators (-25)
        if (hasSpamIndicators(review.getComment())) {
            score -= 25;
            flags.add("Spam indicators detected");
        }

        // 5. Role Verification Bonus (+10 if enterprise role)
        if (isEnterpriseRole(review.getReviewerRole())) {
            score += 10;
        }

        // Cap score between 0-100
        score = Math.max(0, Math.min(100, score));

        String status = score >= 70 ? "VERIFIED" : "NEEDS_REVIEW";
        String summary = generateSummary(score, flags);

        return VerificationResult.builder()
                .score(score)
                .status(status)
                .flags(flags)
                .summary(summary)
                .build();
    }

    private boolean isDuplicate(Review review) {
        List<Review> existingReviews = reviewRepo.findByLaunchId(review.getLaunchId());
        
        for (Review existing : existingReviews) {
            if (existing.getId() != null && existing.getId().equals(review.getId())) {
                continue;
            }
            
            // Simple similarity: check if >80% words match
            if (calculateSimilarity(review.getComment(), existing.getComment()) > 0.8) {
                return true;
            }
        }
        return false;
    }

    private double calculateSimilarity(String text1, String text2) {
        Set<String> words1 = new HashSet<>(Arrays.asList(text1.toLowerCase().split("\\s+")));
        Set<String> words2 = new HashSet<>(Arrays.asList(text2.toLowerCase().split("\\s+")));
        
        Set<String> intersection = new HashSet<>(words1);
        intersection.retainAll(words2);
        
        Set<String> union = new HashSet<>(words1);
        union.addAll(words2);
        
        return union.isEmpty() ? 0 : (double) intersection.size() / union.size();
    }

    private boolean hasSpamIndicators(String comment) {
        String lower = comment.toLowerCase();
        
        // Check for excessive caps
        long capsCount = comment.chars().filter(Character::isUpperCase).count();
        if (capsCount > comment.length() * 0.5 && comment.length() > 20) {
            return true;
        }
        
        // Check for URLs (potential spam)
        if (lower.contains("http://") || lower.contains("https://") || lower.contains("www.")) {
            return true;
        }
        
        // Check for repeated characters
        if (comment.matches(".*(.)(\\1{4,}).*")) {
            return true;
        }
        
        return false;
    }

    private boolean isEnterpriseRole(String role) {
        if (role == null) return false;
        String upper = role.toUpperCase();
        return upper.contains("CTO") || upper.contains("CEO") || 
               upper.contains("CXO") || upper.contains("VP") ||
               upper.contains("DIRECTOR") || upper.contains("HEAD");
    }

    private String generateSummary(int score, List<String> flags) {
        if (score >= 90) {
            return "High-quality review with detailed feedback";
        } else if (score >= 70) {
            return "Review verified with minor concerns";
        } else if (score >= 50) {
            return "Review needs manual verification";
        } else {
            return "Low-quality review - " + String.join(", ", flags);
        }
    }
}
