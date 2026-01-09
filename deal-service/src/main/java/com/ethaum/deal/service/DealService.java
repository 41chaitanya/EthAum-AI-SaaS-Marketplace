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

    public DealService(
            DealRepository dealRepo,
            DealApplicationRepository appRepo
    ) {
        this.dealRepo = dealRepo;
        this.appRepo = appRepo;
    }

    // STARTUP creates pilot
    public DealResponse createDeal(
            DealRequest req,
            String ownerEmail
    ) {
        Deal deal = Deal.builder()
                .startupId(req.getStartupId())
                .title(req.getTitle())
                .description(req.getDescription())
                .targetIndustry(req.getTargetIndustry())
                .durationDays(req.getDurationDays())
                .maxSlots(req.getMaxSlots())
                .status("OPEN")
                .ownerEmail(ownerEmail)
                .createdAt(LocalDateTime.now())
                .build();

        return map(dealRepo.save(deal));
    }

    // ENTERPRISE applies
    public void applyToDeal(Long dealId, String enterpriseEmail) {

        DealApplication app = DealApplication.builder()
                .dealId(dealId)
                .enterpriseEmail(enterpriseEmail)
                .status("APPLIED")
                .appliedAt(LocalDateTime.now())
                .build();

        appRepo.save(app);
    }

    // STARTUP accepts enterprise
    public void acceptApplication(
            Long dealId,
            Long applicationId,
            String ownerEmail
    ) {
        Deal deal = dealRepo.findById(dealId)
                .orElseThrow();

        if (!deal.getOwnerEmail().equals(ownerEmail)) {
            throw new RuntimeException("Not authorized");
        }

        DealApplication app = appRepo.findById(applicationId)
                .orElseThrow();

        app.setStatus("ACCEPTED");
        appRepo.save(app);
    }

    public List<DealResponse> openDeals() {
        return dealRepo.findByStatus("OPEN")
                .stream()
                .map(this::map)
                .toList();
    }

    private DealResponse map(Deal d) {
        return DealResponse.builder()
                .id(d.getId())
                .title(d.getTitle())
                .status(d.getStatus())
                .durationDays(d.getDurationDays())
                .maxSlots(d.getMaxSlots())
                .build();
    }
}
