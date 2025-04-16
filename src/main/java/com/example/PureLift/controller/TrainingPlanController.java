package com.example.PureLift.controller;

import com.example.PureLift.entity.Exercise;
import com.example.PureLift.entity.TrainingDay;
import com.example.PureLift.entity.TrainingPlan;
import com.example.PureLift.service.ExerciseService;
import com.example.PureLift.service.TrainingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/training")
@CrossOrigin(origins = "http://localhost:5173")
public class TrainingPlanController {
   private final TrainingService trainingService;
    private final ExerciseService exerciseService;

   private TrainingPlanController(TrainingService trainingService, ExerciseService exerciseService) {
       this.trainingService = trainingService;
       this.exerciseService = exerciseService;
   }
    @GetMapping
    public ResponseEntity<List<TrainingPlan>> getAllTrainingPlans() {
        List<TrainingPlan> plans = trainingService.getAllTrainingPlans(); // Metoda w serwisie
        return ResponseEntity.ok(plans);
    }
    @GetMapping("/{planId}")
    public ResponseEntity<TrainingPlan> getTrainingPlan(@PathVariable Long planId) {
        TrainingPlan trainingPlan = trainingService.getTrainingPlanById(planId);
        if (trainingPlan == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(trainingPlan);
    }
    @GetMapping("/{planId}/{dayId}")
    public ResponseEntity<List<Exercise>> getExercisesForDay(@PathVariable Long planId, @PathVariable Long dayId) {

        TrainingDay trainingDay = trainingService.getTrainingDayById(planId ,dayId);

        if (trainingDay == null) {
            return ResponseEntity.status(404).body(null);
        }

        List<Exercise> exercises = exerciseService.getExercisesByTrainingDay(dayId);
        return ResponseEntity.ok(exercises);
    }




}
