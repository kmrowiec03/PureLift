package com.example.PureLift.controller;

import com.example.PureLift.entity.Exercise;
import com.example.PureLift.entity.TrainingDay;
import com.example.PureLift.entity.TrainingPlan;
import com.example.PureLift.service.ExerciseService;
import com.example.PureLift.service.TrainingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController("/api/training")
public class TrainingPlanController {
   private final TrainingService trainingService;
    private final ExerciseService exerciseService;

   private TrainingPlanController(TrainingService trainingService, ExerciseService exerciseService) {
       this.trainingService = trainingService;
       this.exerciseService = exerciseService;
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
