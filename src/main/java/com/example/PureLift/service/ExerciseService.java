package com.example.PureLift.service;

import com.example.PureLift.entity.Exercise;
import com.example.PureLift.repository.ExerciseRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExerciseService {
    private final ExerciseRepository repository;

    public ExerciseService(ExerciseRepository repository) {
        this.repository = repository;
    }

    public List<Exercise> getExercisesByTrainingDay(Long trainingDayId) {
        return repository.findAll();
    }

    public Exercise createExercise(Exercise exercise) {
        return repository.save(exercise);
    }
    public void updateExerciseWeight(Long exerciseId, Double weight) {
        Exercise exercise = repository.findById(exerciseId)
                .orElseThrow(() -> new RuntimeException("Exercise not found with id: " + exerciseId));
        exercise.setWeight(weight);
        repository.save(exercise);
    }
}
