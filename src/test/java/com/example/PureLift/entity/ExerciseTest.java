package com.example.PureLift.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Proste testy jednostkowe dla encji Exercise
 */
class ExerciseTest {

    private Exercise exercise;

    @BeforeEach
    void setUp() {
        exercise = new Exercise();
    }

    @Test
    void setAndGetWeight_ShouldWorkCorrectly() {
        // Given
        Double weight = 85.5;

        // When
        exercise.setWeight(weight);

        // Then
        assertEquals(weight, exercise.getWeight());
    }

    @Test
    void setAndGetSets_ShouldWorkCorrectly() {
        // Given
        int sets = 4;

        // When
        exercise.setSets(sets);

        // Then
        assertEquals(sets, exercise.getSets());
    }

    @Test
    void setAndGetReps_ShouldWorkCorrectly() {
        // Given
        int reps = 12;

        // When
        exercise.setReps(reps);

        // Then
        assertEquals(reps, exercise.getReps());
    }

    @Test
    void setAndGetCompletedDate_ShouldWorkCorrectly() {
        // Given
        Date completedDate = new Date();

        // When
        exercise.setCompletedDate(completedDate);

        // Then
        assertEquals(completedDate, exercise.getCompletedDate());
    }

    @Test
    void newExercise_ShouldHaveDefaultValues() {
        // Given & When
        Exercise newExercise = new Exercise();

        // Then
        assertNull(newExercise.getId());
        assertNull(newExercise.getWeight());
        assertEquals(0, newExercise.getSets());
        assertEquals(0, newExercise.getReps());
        assertNull(newExercise.getCompletedDate());
    }
}