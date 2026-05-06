package ru.slisarenko.spring_security_jwt.controller;

import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.slisarenko.spring_security_jwt.model.UserEntity;
import ru.slisarenko.spring_security_jwt.service.UserDAOService;

@Slf4j
@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserDAOService userDAOService;

    @GetMapping("/profile")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('SUPER_ADMIN')")
    public Map<String, Object> getProfile(@AuthenticationPrincipal UserEntity user) {
        return Map.of(
                "username", user.getUsername(),
                "role", user.getRole(),
                "accountLocked", !user.isAccountNonLocked()
        );
    }

    @GetMapping("/public-info")
    @PreAuthorize("hasRole('USER')")
    public String getPublicInfo() {
        return "This is public information for all authenticated users";
    }
}
