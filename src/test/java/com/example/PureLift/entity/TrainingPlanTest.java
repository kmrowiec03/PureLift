package com.example.PureLift.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Proste testy jednostkowe dla encji TrainingPlan
 */
class TrainingPlanTest {

    private TrainingPlan trainingPlan;

    @BeforeEach
    void setUp() {
        trainingPlan = new TrainingPlan();
    }

    @Test
    void setAndGetId_ShouldWorkCorrectly() {
        // Given
        Long id = 456L;

        // When
        trainingPlan.setId(id);

        // Then
        assertEquals(id, trainingPlan.getId());
    }

    @Test
    void setAndGetTitle_ShouldWorkCorrectly() {
        // Given
        String title = "Push/Pull/Legs Workout";

        // When
        trainingPlan.setTitle(title);

        // Then
        assertEquals(title, trainingPlan.getTitle());
    }

    @Test
    void setAndGetUser_ShouldWorkCorrectly() {
        // Given
        User user = new User();
        user.setId(1L);
        user.setName("Test User");

        // When
        trainingPlan.setUser(user);

        // Then
        assertEquals(user, trainingPlan.getUser());
        assertEquals("Test User", trainingPlan.getUser().getName());
    }

    @Test
    void newTrainingPlan_ShouldHaveNullValues() {
        // Given & When
        TrainingPlan newPlan = new TrainingPlan();

        // Then
        assertNull(newPlan.getId());
        assertNull(newPlan.getTitle());
        assertNull(newPlan.getUser());
    }
}