package com.example.PureLift.controller;

import com.example.PureLift.dto.TrainingPlanDTO;
import com.example.PureLift.entity.Exercise;
import com.example.PureLift.entity.TrainingDay;
import com.example.PureLift.entity.TrainingPlan;
import com.example.PureLift.service.ExerciseService;
import com.example.PureLift.service.TrainingService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/training")
@CrossOrigin(origins = "http://localhost:5173")
public class TrainingPlanController {
   private final TrainingService trainingService;
   private final ExerciseService exerciseService;

   public TrainingPlanController(TrainingService trainingService, ExerciseService exerciseService) {
       this.trainingService = trainingService;
       this.exerciseService = exerciseService;
   }
    @GetMapping
    public ResponseEntity<List<TrainingPlanDTO>> getAllTrainingPlans() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email = userDetails.getUsername();
        List<TrainingPlanDTO> plans = trainingService.getTrainingPlansByUserEmail(email).stream()
                .map(trainingService::convertToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(plans);
    }
    @GetMapping("/{planId}")
    public ResponseEntity<TrainingPlanDTO> getTrainingPlan(@PathVariable Long planId) {
        TrainingPlan trainingPlan = trainingService.getTrainingPlanById(planId);
        if (trainingPlan == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(trainingService.convertToDTO(trainingPlan));
    }
    @GetMapping("/{planId}/{dayId}")
    public ResponseEntity<List<Exercise>> getExercisesForDay(@PathVariable Long planId, @PathVariable Long dayId) {
        TrainingDay trainingDay = trainingService.getTrainingDayById(planId, dayId);
        if (trainingDay == null) {
            return ResponseEntity.notFound().build();
        }
        List<Exercise> exercises = exerciseService.getExercisesByTrainingDay(dayId);
        return ResponseEntity.ok(exercises);
    }




}
