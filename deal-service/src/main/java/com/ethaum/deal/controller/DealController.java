package com.ethaum.deal.controller;

import com.ethaum.deal.dto.*;
import com.ethaum.deal.security.JwtHeaderUtil;
import com.ethaum.deal.service.DealService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/deals")
public class DealController {

    private final DealService service;
    private final JwtHeaderUtil jwtUtil;

    public DealController(DealService service, JwtHeaderUtil jwtUtil) {
        this.service = service;
        this.jwtUtil = jwtUtil;
    }

    // STARTUP creates deal
    @PostMapping
    public DealResponse createDeal(
            @RequestBody DealRequest req,
            @RequestHeader("Authorization") String auth
    ) {
        String token = auth.replace("Bearer ", "");
        String role = jwtUtil.extractRole(token);

        if (!role.equals("STARTUP")) {
            throw new RuntimeException("Only startups can create deals");
        }

        return service.createDeal(req, jwtUtil.extractEmail(token));
    }

    // ENTERPRISE applies with AI matchmaking
    @PostMapping("/{id}/apply")
    public ApplicationResponse apply(
            @PathVariable Long id,
            @RequestBody ApplyDealRequest req,
            @RequestHeader("Authorization") String auth
    ) {
        String token = auth.replace("Bearer ", "");
        String role = jwtUtil.extractRole(token);

        if (!role.equals("ENTERPRISE")) {
            throw new RuntimeException("Only enterprises can apply");
        }

        return service.applyToDeal(
                id, 
                jwtUtil.extractEmail(token),
                req.getEnterpriseIndustry(),
                req.getEnterpriseCompanySize()
        );
    }

    // Get applications for a deal (sorted by match score)
    @GetMapping("/{dealId}/applications")
    public List<ApplicationResponse> getApplications(
            @PathVariable Long dealId,
            @RequestHeader("Authorization") String auth
    ) {
        return service.getApplications(dealId);
    }

    // STARTUP accepts enterprise
    @PostMapping("/{dealId}/applications/{appId}/accept")
    public void accept(
            @PathVariable Long dealId,
            @PathVariable Long appId,
            @RequestHeader("Authorization") String auth
    ) {
        String token = auth.replace("Bearer ", "");
        service.acceptApplication(dealId, appId, jwtUtil.extractEmail(token));
    }

    // Public
    @GetMapping("/open")
    public List<DealResponse> openDeals() {
        return service.openDeals();
    }
}
