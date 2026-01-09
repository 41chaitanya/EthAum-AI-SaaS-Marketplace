package com.ethaum.launch.service;

import com.ethaum.launch.dto.TemplateRequest;
import com.ethaum.launch.dto.TemplateResponse;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Service
public class AITemplateService {

    private static final Map<String, String[]> TAGLINE_TEMPLATES = Map.of(
        "AI/ML", new String[]{
            "%s - AI that works for you",
            "Intelligent %s for modern teams",
            "%s: Where AI meets simplicity",
            "Smarter decisions with %s"
        },
        "FinTech", new String[]{
            "%s - Finance, reimagined",
            "Smart money moves with %s",
            "%s: Your financial co-pilot",
            "Banking on %s for growth"
        },
        "HealthTech", new String[]{
            "%s - Healthcare that cares",
            "Better health starts with %s",
            "%s: Wellness, simplified",
            "Healing innovation with %s"
        },
        "SaaS", new String[]{
            "%s - Built for scale",
            "Work smarter with %s",
            "%s: Software that gets it",
            "Elevate your workflow with %s"
        },
        "EdTech", new String[]{
            "%s - Learn without limits",
            "Education evolved with %s",
            "%s: Knowledge, unlocked",
            "Future-ready learning with %s"
        },
        "MarTech", new String[]{
            "%s - Marketing that converts",
            "Grow faster with %s",
            "%s: Your growth engine",
            "Scale your reach with %s"
        }
    );

    private static final Map<String, String> DESCRIPTION_TEMPLATES = Map.of(
        "B2B", "%s helps businesses %s with an intuitive platform designed for teams. Built for scale, loved by enterprises.",
        "B2C", "%s empowers individuals to %s effortlessly. Simple, powerful, and designed for you.",
        "Enterprise", "%s delivers enterprise-grade %s solutions with security, compliance, and scale built-in."
    );

    private static final String[] DEFAULT_TAGLINES = {
        "%s - Innovation simplified",
        "The future is %s",
        "%s: Built different"
    };

    public TemplateResponse generateTemplates(TemplateRequest req) {
        String[] taglines = TAGLINE_TEMPLATES.getOrDefault(
            req.getCategory(), 
            DEFAULT_TAGLINES
        );
        
        String primaryTagline = String.format(taglines[0], req.getProductName());
        
        List<String> alternatives = Arrays.stream(taglines)
            .skip(1)
            .map(t -> String.format(t, req.getProductName()))
            .toList();

        String descTemplate = DESCRIPTION_TEMPLATES.getOrDefault(
            req.getIndustry(), 
            "%s helps you %s with ease and efficiency."
        );
        
        String problem = req.getProblemSolved() != null && !req.getProblemSolved().isBlank()
            ? req.getProblemSolved() 
            : "achieve more";
            
        String description = String.format(descTemplate, 
            req.getProductName(), problem);

        String valueProp = generateValueProposition(req, problem);

        return TemplateResponse.builder()
            .suggestedTagline(primaryTagline)
            .suggestedDescription(description)
            .valueProposition(valueProp)
            .alternativeTaglines(alternatives)
            .build();
    }

    private String generateValueProposition(TemplateRequest req, String problem) {
        String industry = req.getIndustry() != null ? req.getIndustry() : "modern";
        String category = req.getCategory() != null ? req.getCategory() : "innovative";
        
        return String.format(
            "With %s, %s teams can %s faster, smarter, and more efficiently. " +
            "Powered by %s technology, trusted by industry leaders.",
            req.getProductName(),
            industry,
            problem,
            category
        );
    }
}
