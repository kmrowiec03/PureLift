package com.example.PureLift.service;

import com.example.PureLift.entity.Exercise;
import com.example.PureLift.repository.ExerciseRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.easymock.EasyMock.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Testy jednostkowe ExerciseService z EasyMock
 */
class ExerciseServiceTest {

    private ExerciseRepository mockExerciseRepository;
    private ExerciseService exerciseService;
    private Exercise testExercise;

    @BeforeEach
    void setUp() {
        mockExerciseRepository = createMock(ExerciseRepository.class);
        exerciseService = new ExerciseService(mockExerciseRepository);
        
        testExercise = new Exercise();
        testExercise.setId(1L);
        testExercise.setWeight(100.0);
        testExercise.setSets(4);
        testExercise.setReps(8);
    }

    @AfterEach
    void tearDown() {
        verify(mockExerciseRepository);
    }

    @Test
    void createExercise_ShouldSaveExercise() {
        // Given
        Exercise newExercise = new Exercise();
        newExercise.setWeight(80.0);
        expect(mockExerciseRepository.save(newExercise)).andReturn(newExercise);
        replay(mockExerciseRepository);

        // When
        Exercise result = exerciseService.createExercise(newExercise);

        // Then
        assertEquals(80.0, result.getWeight());
    }

    @Test
    void getExercisesByTrainingDay_ShouldReturnAllExercises() {
        // Given
        List<Exercise> exercises = Arrays.asList(testExercise);
        expect(mockExerciseRepository.findAll()).andReturn(exercises);
        replay(mockExerciseRepository);

        // When
        List<Exercise> result = exerciseService.getExercisesByTrainingDay(1L);

        // Then
        assertEquals(1, result.size());
        assertEquals(100.0, result.get(0).getWeight());
    }

    @Test
    void updateExerciseWeight_ShouldUpdateAndSave() {
        
        expect(mockExerciseRepository.findById(1L)).andReturn(Optional.of(testExercise));
        expect(mockExerciseRepository.save(testExercise)).andReturn(testExercise);
        replay(mockExerciseRepository);

        
        exerciseService.updateExerciseWeight(1L, 120.0);

        assertEquals(120.0, testExercise.getWeight());
    }

    @Test
    void updateExerciseSets_ShouldUpdateSets() {
        // Given
        expect(mockExerciseRepository.findById(1L)).andReturn(Optional.of(testExercise));
        expect(mockExerciseRepository.save(testExercise)).andReturn(testExercise);
        replay(mockExerciseRepository);

        // When
        exerciseService.updateExerciseSets(1L, 5);

        // Then
        assertEquals(5, testExercise.getSets());
    }

    @Test
    void updateExerciseReps_ShouldUpdateReps() {
        // Given
        expect(mockExerciseRepository.findById(1L)).andReturn(Optional.of(testExercise));
        expect(mockExerciseRepository.save(testExercise)).andReturn(testExercise);
        replay(mockExerciseRepository);

        // When
        exerciseService.updateExerciseReps(1L, 12);

        // Then
        assertEquals(12, testExercise.getReps());
    }

    @Test
    void markExerciseCompletion_ShouldSetCompletionDate() {
        // Given
        expect(mockExerciseRepository.findById(1L)).andReturn(Optional.of(testExercise));
        expect(mockExerciseRepository.save(testExercise)).andReturn(testExercise);
        replay(mockExerciseRepository);

        // When
        exerciseService.markExerciseCompletion(1L, true);

        // Then
        assertNotNull(testExercise.getCompletedDate());
    }

    @Test
    void updateExercise_ShouldUpdateAllFields() {
        // Given
        expect(mockExerciseRepository.findById(1L)).andReturn(Optional.of(testExercise));
        expect(mockExerciseRepository.save(testExercise)).andReturn(testExercise);
        replay(mockExerciseRepository);

        // When
        exerciseService.updateExercise(1L, 6, 10, 150.0);

        // Then
        assertEquals(6, testExercise.getSets());
        assertEquals(10, testExercise.getReps());
        assertEquals(150.0, testExercise.getWeight());
    }
}