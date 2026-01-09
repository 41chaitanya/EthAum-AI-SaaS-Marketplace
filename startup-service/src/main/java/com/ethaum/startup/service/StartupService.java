package com.ethaum.startup.service;

import com.ethaum.startup.dto.*;
import com.ethaum.startup.model.Startup;
import com.ethaum.startup.repository.StartupRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class StartupService {

    private final StartupRepository repo;

    public StartupService(StartupRepository repo) {
        this.repo = repo;
    }

    public StartupResponse createStartup(
            StartupRequest req,
            String ownerEmail
    ) {

        if (repo.findByOwnerEmail(ownerEmail).isPresent()) {
            throw new RuntimeException("Startup already exists for this user");
        }

        Startup startup = Startup.builder()
                .name(req.getName())
                .website(req.getWebsite())
                .industry(req.getIndustry())
                .fundingStage(req.getFundingStage())
                .arrRange(req.getArrRange())
                .description(req.getDescription())
                .ownerEmail(ownerEmail)
                .createdAt(LocalDateTime.now())
                .build();

        Startup saved = repo.save(startup);

        return mapToResponse(saved);
    }

    public StartupResponse getMyStartup(String ownerEmail) {
        Startup startup = repo.findByOwnerEmail(ownerEmail)
                .orElseThrow(() -> new RuntimeException("Startup not found"));

        return mapToResponse(startup);
    }

    private StartupResponse mapToResponse(Startup s) {
        return StartupResponse.builder()
                .id(s.getId())
                .name(s.getName())
                .website(s.getWebsite())
                .industry(s.getIndustry())
                .fundingStage(s.getFundingStage())
                .arrRange(s.getArrRange())
                .description(s.getDescription())
                .build();
    }
}
