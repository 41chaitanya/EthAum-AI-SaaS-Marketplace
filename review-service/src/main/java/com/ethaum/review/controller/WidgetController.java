package com.ethaum.review.controller;

import com.ethaum.review.dto.WidgetData;
import com.ethaum.review.service.WidgetService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/widgets")
@CrossOrigin(origins = "*") // Allow embedding from any domain
public class WidgetController {

    private final WidgetService service;

    public WidgetController(WidgetService service) {
        this.service = service;
    }

    // Get widget data as JSON
    @GetMapping("/{launchId}/data")
    public WidgetData getWidgetData(@PathVariable Long launchId) {
        return service.getWidgetData(launchId);
    }

    // Get full embeddable widget HTML
    @GetMapping(value = "/{launchId}/embed", produces = MediaType.TEXT_HTML_VALUE)
    public String getEmbedCode(
            @PathVariable Long launchId,
            @RequestParam(defaultValue = "http://localhost:8084") String baseUrl
    ) {
        return service.generateEmbedCode(launchId, baseUrl);
    }

    // Get badge-only embed (smaller)
    @GetMapping(value = "/{launchId}/badge", produces = MediaType.TEXT_HTML_VALUE)
    public String getBadge(@PathVariable Long launchId) {
        return service.generateBadgeOnly(launchId);
    }
}
