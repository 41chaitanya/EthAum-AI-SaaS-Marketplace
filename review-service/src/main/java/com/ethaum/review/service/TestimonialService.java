package com.ethaum.review.service;

import com.ethaum.review.dto.*;
import com.ethaum.review.model.Testimonial;
import com.ethaum.review.repository.TestimonialRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TestimonialService {

    private final TestimonialRepository repo;

    public TestimonialService(TestimonialRepository repo) {
        this.repo = repo;
    }

    public TestimonialResponse submit(TestimonialRequest req, String email) {
        Testimonial t = Testimonial.builder()
                .startupId(req.getStartupId())
                .launchId(req.getLaunchId())
                .type(req.getType())
                .videoUrl(req.getVideoUrl())
                .caseStudyTitle(req.getCaseStudyTitle())
                .caseStudyText(req.getCaseStudyText())
                .submitterEmail(email)
                .submitterName(req.getSubmitterName())
                .submitterRole(req.getSubmitterRole())
                .companyName(req.getCompanyName())
                .status("PENDING")
                .submittedAt(LocalDateTime.now())
                .build();

        return map(repo.save(t));
    }

    public List<TestimonialResponse> getApproved(Long startupId) {
        return repo.findByStartupIdAndStatus(startupId, "APPROVED")
                .stream()
                .map(this::map)
                .toList();
    }

    public List<TestimonialResponse> getPending() {
        return repo.findByStatus("PENDING")
                .stream()
                .map(this::map)
                .toList();
    }

    public TestimonialResponse moderate(Long id, String status, String moderatorEmail) {
        Testimonial t = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Testimonial not found"));

        t.setStatus(status);
        t.setModeratedAt(LocalDateTime.now());
        t.setModeratedBy(moderatorEmail);

        return map(repo.save(t));
    }

    private TestimonialResponse map(Testimonial t) {
        return TestimonialResponse.builder()
                .id(t.getId())
                .type(t.getType())
                .videoUrl(t.getVideoUrl())
                .caseStudyTitle(t.getCaseStudyTitle())
                .caseStudyText(t.getCaseStudyText())
                .submitterName(t.getSubmitterName())
                .submitterRole(t.getSubmitterRole())
                .companyName(t.getCompanyName())
                .status(t.getStatus())
                .submittedAt(t.getSubmittedAt())
                .build();
    }
}
