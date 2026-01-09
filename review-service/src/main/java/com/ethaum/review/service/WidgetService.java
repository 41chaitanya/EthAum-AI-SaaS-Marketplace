package com.ethaum.review.service;

import com.ethaum.review.dto.WidgetData;
import com.ethaum.review.model.Review;
import com.ethaum.review.repository.ReviewRepository;
import com.ethaum.review.repository.TestimonialRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WidgetService {

    private final ReviewRepository reviewRepo;
    private final TestimonialRepository testimonialRepo;

    public WidgetService(ReviewRepository reviewRepo, TestimonialRepository testimonialRepo) {
        this.reviewRepo = reviewRepo;
        this.testimonialRepo = testimonialRepo;
    }

    public WidgetData getWidgetData(Long launchId) {
        List<Review> reviews = reviewRepo.findByLaunchId(launchId);
        
        double avgRating = reviews.stream()
                .mapToInt(Review::getRating)
                .average()
                .orElse(0.0);
        
        int reviewCount = reviews.size();
        long enterpriseCount = reviews.stream()
                .filter(r -> isEnterprise(r.getCompanySize()))
                .count();
        
        String badge = determineBadge(reviewCount, avgRating, enterpriseCount);
        
        return WidgetData.builder()
                .launchId(launchId)
                .averageRating(Math.round(avgRating * 10.0) / 10.0)
                .reviewCount(reviewCount)
                .enterpriseReviewCount((int) enterpriseCount)
                .badge(badge)
                .badgeColor(getBadgeColor(badge))
                .build();
    }

    public String generateEmbedCode(Long launchId, String baseUrl) {
        WidgetData data = getWidgetData(launchId);
        
        return String.format("""
            <!-- EthAum Credibility Widget -->
            <div id="ethaum-widget-%d" style="font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif; 
                 border: 1px solid #e0e0e0; border-radius: 8px; padding: 16px; max-width: 280px; background: #fff;">
              <div style="display: flex; align-items: center; gap: 8px; margin-bottom: 12px;">
                <svg width="24" height="24" viewBox="0 0 24 24" fill="%s">
                  <path d="M12 2L15.09 8.26L22 9.27L17 14.14L18.18 21.02L12 17.77L5.82 21.02L7 14.14L2 9.27L8.91 8.26L12 2Z"/>
                </svg>
                <span style="font-size: 24px; font-weight: bold; color: #333;">%.1f</span>
                <span style="color: #666; font-size: 14px;">/ 5.0</span>
              </div>
              <div style="font-size: 13px; color: #666; margin-bottom: 8px;">
                Based on <strong>%d reviews</strong>
              </div>
              <div style="display: inline-block; background: %s; color: white; padding: 4px 12px; 
                   border-radius: 12px; font-size: 12px; font-weight: 500;">
                %s
              </div>
              <div style="margin-top: 12px; font-size: 11px; color: #999;">
                Verified by <a href="https://ethaum.ai" style="color: #6366f1; text-decoration: none;">EthAum.ai</a>
              </div>
            </div>
            <script>
              // Optional: Load dynamic data
              // fetch('%s/widgets/%d/data').then(r => r.json()).then(console.log);
            </script>
            <!-- End EthAum Widget -->
            """,
            launchId,
            data.getBadgeColor(),
            data.getAverageRating(),
            data.getReviewCount(),
            data.getBadgeColor(),
            data.getBadge(),
            baseUrl,
            launchId
        );
    }

    public String generateBadgeOnly(Long launchId) {
        WidgetData data = getWidgetData(launchId);
        
        return String.format("""
            <a href="https://ethaum.ai/launch/%d" target="_blank" 
               style="display: inline-flex; align-items: center; gap: 6px; padding: 6px 12px;
                      background: %s; color: white; border-radius: 6px; text-decoration: none;
                      font-family: -apple-system, sans-serif; font-size: 13px; font-weight: 500;">
              <svg width="16" height="16" viewBox="0 0 24 24" fill="white">
                <path d="M9 16.17L4.83 12l-1.42 1.41L9 19 21 7l-1.41-1.41z"/>
              </svg>
              %s
            </a>
            """,
            launchId,
            data.getBadgeColor(),
            data.getBadge()
        );
    }

    private String determineBadge(int reviewCount, double avgRating, long enterpriseCount) {
        if (enterpriseCount >= 5 && avgRating >= 4.5) {
            return "Trusted by Enterprises";
        } else if (reviewCount >= 10 && avgRating >= 4.0) {
            return "EthAum Top Rated";
        } else if (reviewCount >= 5 && avgRating >= 3.5) {
            return "EthAum Verified Reviews";
        } else if (reviewCount >= 1) {
            return "EthAum Verified";
        }
        return "New on EthAum";
    }

    private String getBadgeColor(String badge) {
        return switch (badge) {
            case "Trusted by Enterprises" -> "#7c3aed";
            case "EthAum Top Rated" -> "#059669";
            case "EthAum Verified Reviews" -> "#2563eb";
            case "EthAum Verified" -> "#6366f1";
            default -> "#9ca3af";
        };
    }

    private boolean isEnterprise(String companySize) {
        return "ENTERPRISE".equalsIgnoreCase(companySize);
    }
}
