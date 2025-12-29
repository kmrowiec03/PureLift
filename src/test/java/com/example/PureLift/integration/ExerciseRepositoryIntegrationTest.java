package com.example.PureLift.integration;

import com.example.PureLift.entity.Exercise;
import com.example.PureLift.entity.ExerciseTemplate;
import com.example.PureLift.entity.TrainingDay;
import com.example.PureLift.entity.TrainingPlan;
import com.example.PureLift.entity.User;
import com.example.PureLift.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testy integracyjne ExerciseRepository z bazą danych
 * Sprawdzanie zapisu i odczytu ćwiczeń
 */
@DataJpaTest
@ActiveProfiles("test")
class ExerciseRepositoryIntegrationTest {

    @Autowired
    private ExerciseRepository exerciseRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TrainingPlanRepository trainingPlanRepository;

    @Autowired
    private TrainingDayRepository trainingDayRepository;

    @Autowired
    private ExerciseTemplateRepository exerciseTemplateRepository;

    private User testUser;
    private TrainingPlan testPlan;
    private TrainingDay testDay;
    private ExerciseTemplate testTemplate;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setEmail("exercise@test.com");
        testUser.setName("Exercise User");
        testUser.setPassword("password");
        testUser = userRepository.save(testUser);

        testPlan = new TrainingPlan();
        testPlan.setTitle("Test Plan");
        testPlan.setUser(testUser);
        testPlan = trainingPlanRepository.save(testPlan);

        testDay = new TrainingDay();
        testDay.setDayNumber(1);
        testDay.setWeekNumber(1);
        testDay.setTrainingPlan(testPlan);
        testDay = trainingDayRepository.save(testDay);

        testTemplate = new ExerciseTemplate();
        testTemplate.setName("Bench Press");
        testTemplate = exerciseTemplateRepository.save(testTemplate);
    }

    @Test
    void saveExercise_ShouldPersistInDatabase() {
        // Given
        Exercise exercise = new Exercise();
        exercise.setTrainingDay(testDay);
        exercise.setExerciseTemplate(testTemplate);
        exercise.setWeight(80.0);
        exercise.setReps(10);
        exercise.setSets(3);

        // When
        Exercise saved = exerciseRepository.save(exercise);

        // Then
        assertNotNull(saved.getId());
        assertEquals(80.0, saved.getWeight());
        assertEquals(10, saved.getReps());
        assertEquals(3, saved.getSets());
        assertEquals(testTemplate.getName(), saved.getExerciseTemplate().getName());
    }

    @Test
    void findAll_ShouldReturnAllExercises() {
        // Given
        Exercise exercise1 = new Exercise();
        exercise1.setTrainingDay(testDay);
        exercise1.setExerciseTemplate(testTemplate);
        exercise1.setWeight(100.0);
        exercise1.setReps(8);
        exercise1.setSets(4);

        Exercise exercise2 = new Exercise();
        exercise2.setTrainingDay(testDay);
        exercise2.setExerciseTemplate(testTemplate);
        exercise2.setWeight(120.0);
        exercise2.setReps(5);
        exercise2.setSets(3);

        exerciseRepository.save(exercise1);
        exerciseRepository.save(exercise2);

        // When
        List<Exercise> exercises = exerciseRepository.findAll();

        // Then
        assertTrue(exercises.size() >= 2);
        assertTrue(exercises.stream().anyMatch(ex -> ex.getWeight().equals(100.0)));
        assertTrue(exercises.stream().anyMatch(ex -> ex.getWeight().equals(120.0)));
    }

    @Test
    void updateExerciseWeight_ShouldPersistChanges() {
        // Given
        Exercise exercise = new Exercise();
        exercise.setTrainingDay(testDay);
        exercise.setExerciseTemplate(testTemplate);
        exercise.setWeight(50.0);
        exercise.setReps(12);
        exercise.setSets(3);
        Exercise saved = exerciseRepository.save(exercise);

        // When
        saved.setWeight(60.0);
        Exercise updated = exerciseRepository.save(saved);

        // Then
        assertEquals(60.0, updated.getWeight());
        
        // Verify in database
        Exercise found = exerciseRepository.findById(saved.getId()).orElse(null);
        assertEquals(60.0, found.getWeight());
    }

    @Test
    void deleteExercise_ShouldRemoveFromDatabase() {
        // Given
        Exercise exercise = new Exercise();
        exercise.setTrainingDay(testDay);
        exercise.setExerciseTemplate(testTemplate);
        exercise.setWeight(70.0);
        exercise.setReps(15);
        exercise.setSets(2);
        Exercise saved = exerciseRepository.save(exercise);

        // When
        exerciseRepository.deleteById(saved.getId());

        // Then
        assertFalse(exerciseRepository.findById(saved.getId()).isPresent());
    }
}