package com.ethaum.deal.controller;

import com.ethaum.deal.dto.MatchScoreResponse;
import com.ethaum.deal.service.MatchmakingService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/matchmaking")
public class MatchmakingController {

    private final MatchmakingService service;

    public MatchmakingController(MatchmakingService service) {
        this.service = service;
    }

    @GetMapping("/score")
    public MatchScoreResponse calculateMatch(
            @RequestParam Long dealId,
            @RequestParam Long applicationId,
            @RequestParam String enterpriseEmail,
            @RequestParam String startupIndustry,
            @RequestParam String targetIndustry,
            @RequestParam(defaultValue = "50") double startupCredibilityScore,
            @RequestParam String enterpriseIndustry,
            @RequestParam(defaultValue = "MID") String enterpriseCompanySize
    ) {
        return service.calculateMatch(
                dealId, applicationId, enterpriseEmail,
                startupIndustry, targetIndustry, startupCredibilityScore,
                enterpriseIndustry, enterpriseCompanySize
        );
    }
}
