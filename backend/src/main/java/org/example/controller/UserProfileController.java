package org.example.controller;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import org.example.entity.UserProfile;
import org.example.log.LogAnalyzer;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
@RestController
@RequestMapping("/profiles")
public class UserProfileController {
    private final LogAnalyzer logAnalyzer;

    public UserProfileController(LogAnalyzer logAnalyzer) {
        this.logAnalyzer = logAnalyzer;
    }

    @GetMapping
    public ResponseEntity<List<UserProfile>> fetchAllUserProfiles() {
        List<UserProfile> sortedProfiles = logAnalyzer.analyzeUserActions().values().stream().sorted(Comparator.comparing(UserProfile::getTotalActions).reversed()).collect(Collectors.toList());
        return ResponseEntity.ok(sortedProfiles);
    }
}
