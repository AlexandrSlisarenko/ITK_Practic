package ru.slisarenko.spring_security_jwt.controller;

import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/moderator")
@RequiredArgsConstructor
public class ModeratorController {

    @GetMapping("/content")
    @PreAuthorize("hasRole('MODERATOR') or hasRole('SUPER_ADMIN')")
    public Map<String, Object> getContentToModerate() {
        return Map.of(
                "pendingReviews", 5,
                "reportedContent", 3,
                "status", "moderation_panel"
        );
    }

    @PostMapping("/approve/{contentId}")
    @PreAuthorize("hasRole('MODERATOR') or hasRole('SUPER_ADMIN')")
    public String approveContent(@PathVariable Long contentId) {
        return "Content " + contentId + " approved by moderator";
    }

    @PostMapping("/reject/{contentId}")
    @PreAuthorize("hasRole('MODERATOR') or hasRole('SUPER_ADMIN')")
    public String rejectContent(@PathVariable Long contentId) {
        return "Content " + contentId + " rejected by moderator";
    }
}
