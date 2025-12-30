package com.example.PureLift.controller;

import com.example.PureLift.dto.MaxWeightExerciseDTO;
import com.example.PureLift.dto.RecentExerciseDTO;
import com.example.PureLift.dto.TonnageDataDTO;
import com.example.PureLift.entity.User;
import com.example.PureLift.service.DataPopulationService;
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
    private final DataPopulationService dataPopulationService;
    
    public MetricsController(MetricsService metricsService, DataPopulationService dataPopulationService) {
        this.metricsService = metricsService;
        this.dataPopulationService = dataPopulationService;
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
    
    @GetMapping("/max-weights")
    public ResponseEntity<List<MaxWeightExerciseDTO>> getMaxWeights(@AuthenticationPrincipal User currentUser) {
        List<MaxWeightExerciseDTO> maxWeights = metricsService.getMaxWeightsForUser(currentUser.getId());
        return ResponseEntity.ok(maxWeights);
    }
    
    @PostMapping("/populate-test-data")
    public ResponseEntity<String> populateTestData() {
        dataPopulationService.populateExerciseData();
        String summary = dataPopulationService.getPopulationSummary();
        return ResponseEntity.ok(summary);
    }
    
    @PostMapping("/create-test-users")
    public ResponseEntity<String> createTestUsers() {
        dataPopulationService.createTestUsers();
        String summary = dataPopulationService.getUserCreationSummary();
        return ResponseEntity.ok(summary);
    }
}
