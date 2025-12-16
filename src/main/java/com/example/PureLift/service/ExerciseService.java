package com.example.PureLift.service;

import com.example.PureLift.entity.Exercise;
import com.example.PureLift.repository.ExerciseRepository;
import org.springframework.stereotype.Service;

import java.util.Date;
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

    public void updateExerciseSets(Long exerciseId, Integer sets) {
        Exercise exercise = repository.findById(exerciseId)
                .orElseThrow(() -> new RuntimeException("Exercise not found with id: " + exerciseId));
        exercise.setSets(sets);
        repository.save(exercise);
    }

    public void updateExerciseReps(Long exerciseId, Integer reps) {
        Exercise exercise = repository.findById(exerciseId)
                .orElseThrow(() -> new RuntimeException("Exercise not found with id: " + exerciseId));
        exercise.setReps(reps);
        repository.save(exercise);
    }

    public void updateExercise(Long exerciseId, Integer sets, Integer reps, Double weight) {
        Exercise exercise = repository.findById(exerciseId)
                .orElseThrow(() -> new RuntimeException("Exercise not found with id: " + exerciseId));

        if (sets != null) {
            exercise.setSets(sets);
        }
        if (reps != null) {
            exercise.setReps(reps);
        }
        if (weight != null) {
            exercise.setWeight(weight);
        }

        repository.save(exercise);
    }

    public void markExerciseCompletion(Long exerciseId, boolean completed) {
        Exercise exercise = repository.findById(exerciseId)
                .orElseThrow(() -> new RuntimeException("Exercise not found with id: " + exerciseId));

        if (completed) {
            // Oznacz jako ukończone z aktualną datą
            exercise.setCompletedDate(new Date());
        } else {
            // Usuń oznaczenie ukończenia
            exercise.setCompletedDate(null);
        }

        repository.save(exercise);
    }
}
