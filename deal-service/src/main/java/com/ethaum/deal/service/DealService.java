package com.ethaum.deal.service;

import com.ethaum.deal.dto.*;
import com.ethaum.deal.model.*;
import com.ethaum.deal.repository.*;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class DealService {

    private final DealRepository dealRepo;
    private final DealApplicationRepository appRepo;
    private final MatchmakingService matchmakingService;

    public DealService(
            DealRepository dealRepo,
            DealApplicationRepository appRepo,
            MatchmakingService matchmakingService
    ) {
        this.dealRepo = dealRepo;
        this.appRepo = appRepo;
        this.matchmakingService = matchmakingService;
    }

    public DealResponse createDeal(DealRequest req, String ownerEmail) {
        Deal deal = Deal.builder()
                .startupId(req.getStartupId())
                .title(req.getTitle())
                .description(req.getDescription())
                .startupIndustry(req.getStartupIndustry())
                .targetIndustry(req.getTargetIndustry())
                .startupCredibilityScore(req.getStartupCredibilityScore())
                .durationDays(req.getDurationDays())
                .maxSlots(req.getMaxSlots())
                .status("OPEN")
                .ownerEmail(ownerEmail)
                .createdAt(LocalDateTime.now())
                .build();

        return map(dealRepo.save(deal));
    }

    public ApplicationResponse applyToDeal(
            Long dealId, 
            String enterpriseEmail,
            String enterpriseIndustry,
            String enterpriseCompanySize
    ) {
        Deal deal = dealRepo.findById(dealId)
                .orElseThrow(() -> new RuntimeException("Deal not found"));

        // Calculate match score
        MatchScoreResponse matchScore = matchmakingService.calculateMatch(
                dealId, null, enterpriseEmail,
                deal.getStartupIndustry(),
                deal.getTargetIndustry(),
                deal.getStartupCredibilityScore(),
                enterpriseIndustry,
                enterpriseCompanySize
        );

        DealApplication app = DealApplication.builder()
                .dealId(dealId)
                .enterpriseEmail(enterpriseEmail)
                .enterpriseIndustry(enterpriseIndustry)
                .enterpriseCompanySize(enterpriseCompanySize)
                .matchScore(matchScore.getMatchScore())
                .matchLabel(matchScore.getMatchLabel())
                .status("APPLIED")
                .appliedAt(LocalDateTime.now())
                .build();

        DealApplication saved = appRepo.save(app);

        return ApplicationResponse.builder()
                .applicationId(saved.getId())
                .dealId(dealId)
                .enterpriseEmail(enterpriseEmail)
                .status(saved.getStatus())
                .matchScore(saved.getMatchScore())
                .matchLabel(saved.getMatchLabel())
                .matchReasons(matchScore.getMatchReasons())
                .build();
    }

    public void acceptApplication(Long dealId, Long applicationId, String ownerEmail) {
        Deal deal = dealRepo.findById(dealId).orElseThrow();

        if (!deal.getOwnerEmail().equals(ownerEmail)) {
            throw new RuntimeException("Not authorized");
        }

        DealApplication app = appRepo.findById(applicationId).orElseThrow();
        app.setStatus("ACCEPTED");
        appRepo.save(app);
    }

    public List<DealResponse> openDeals() {
        return dealRepo.findByStatus("OPEN")
                .stream()
                .map(this::map)
                .toList();
    }

    public List<ApplicationResponse> getApplications(Long dealId) {
        return appRepo.findByDealIdOrderByMatchScoreDesc(dealId)
                .stream()
                .map(this::mapApp)
                .toList();
    }

    private DealResponse map(Deal d) {
        return DealResponse.builder()
                .id(d.getId())
                .title(d.getTitle())
                .status(d.getStatus())
                .targetIndustry(d.getTargetIndustry())
                .durationDays(d.getDurationDays())
                .maxSlots(d.getMaxSlots())
                .build();
    }

    private ApplicationResponse mapApp(DealApplication a) {
        return ApplicationResponse.builder()
                .applicationId(a.getId())
                .dealId(a.getDealId())
                .enterpriseEmail(a.getEnterpriseEmail())
                .status(a.getStatus())
                .matchScore(a.getMatchScore())
                .matchLabel(a.getMatchLabel())
                .build();
    }
}
