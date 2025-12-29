package com.example.PureLift.controller;

import com.example.PureLift.dto.RecentExerciseDTO;
import com.example.PureLift.dto.TonnageDataDTO;
import com.example.PureLift.entity.User;
import com.example.PureLift.service.MetricsService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/metrics")
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class MetricsController {
    
    private final MetricsService metricsService;
    
    public MetricsController(MetricsService metricsService) {
        this.metricsService = metricsService;
    }
    
    @GetMapping("/recent-exercises")
    public ResponseEntity<List<RecentExerciseDTO>> getRecentExercises(@AuthenticationPrincipal User currentUser) {
        List<RecentExerciseDTO> exercises = metricsService.getRecentExercises(currentUser.getId());
        return ResponseEntity.ok(exercises);
    }
    
    @GetMapping("/tonnage")
    public ResponseEntity<List<TonnageDataDTO>> getTonnageData(
            @RequestParam Long exerciseId,
            @RequestParam(defaultValue = "12") Integer weeks,
            @AuthenticationPrincipal User currentUser) {
        List<TonnageDataDTO> tonnageData = metricsService.getTonnageData(currentUser.getId(), exerciseId, weeks);
        return ResponseEntity.ok(tonnageData);
    }
}
