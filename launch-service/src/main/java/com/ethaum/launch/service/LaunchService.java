package com.ethaum.launch.service;

import com.ethaum.launch.dto.*;
import com.ethaum.launch.model.Launch;
import com.ethaum.launch.repository.LaunchRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class LaunchService {

    private final LaunchRepository repo;

    public LaunchService(LaunchRepository repo) {
        this.repo = repo;
    }

    public LaunchResponse createLaunch(
            LaunchRequest req,
            String ownerEmail
    ) {
        Launch launch = Launch.builder()
                .startupId(req.getStartupId())
                .productName(req.getProductName())
                .tagline(req.getTagline())
                .description(req.getDescription())
                .category(req.getCategory())
                .upvotes(0)
                .commentsCount(0)
                .ownerEmail(ownerEmail)
                .launchedAt(LocalDateTime.now())
                .build();

        return map(repo.save(launch));
    }

    public void upvote(Long launchId) {
        Launch launch = repo.findById(launchId)
                .orElseThrow(() -> new RuntimeException("Launch not found"));
        launch.setUpvotes(launch.getUpvotes() + 1);
        repo.save(launch);
    }

    public List<LaunchResponse> trending() {
        return repo.findAllByOrderByUpvotesDesc()
                .stream()
                .map(this::map)
                .toList();
    }

    private LaunchResponse map(Launch l) {
        return LaunchResponse.builder()
                .id(l.getId())
                .productName(l.getProductName())
                .tagline(l.getTagline())
                .category(l.getCategory())
                .upvotes(l.getUpvotes())
                .build();
    }
}
