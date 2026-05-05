package ru.slisarenko.spring_security_jwt.controller;

import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.slisarenko.spring_security_jwt.model.User;
import ru.slisarenko.spring_security_jwt.service.LoginService;
import ru.slisarenko.spring_security_jwt.service.UserDAOService;

@Slf4j
@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {
    private final UserDAOService userRepository;
    private final LoginService loginAttemptService;
    private final UserDAOService userDAOService;

    @GetMapping("/users")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<Page<User>> getAllUsers(@RequestParam("page") int page, @RequestParam("size") int size) {
        return new ResponseEntity<>(userRepository.findAll(page, size), HttpStatus.OK);
    }

    @PutMapping("/unlock/{username}")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<String> unlockAccount(@PathVariable String username) {
        loginAttemptService.unlockAccount(username);
        return new ResponseEntity<>("Account " + username + " unlocked successfully", HttpStatus.OK);
    }

    @DeleteMapping("/user/{username}")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public String deleteUser(@PathVariable String username) {
        userDAOService.deleteUser(username);
        return "User " + username + " deleted";
    }

    @GetMapping("/system/stats")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public Map<String, Object> getSystemStats() {
        return Map.of(
                "totalEnabledAccount", userDAOService.countEnabledAccounts()
        );
    }
}
