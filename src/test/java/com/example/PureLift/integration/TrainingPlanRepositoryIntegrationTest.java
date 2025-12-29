package com.example.PureLift.integration;

import com.example.PureLift.entity.TrainingPlan;
import com.example.PureLift.entity.User;
import com.example.PureLift.repository.TrainingPlanRepository;
import com.example.PureLift.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testy integracyjne TrainingPlanRepository z bazą danych
 * Sprawdzanie zapisu i odczytu planów treningowych
 */
@DataJpaTest
@ActiveProfiles("test")
class TrainingPlanRepositoryIntegrationTest {

    @Autowired
    private TrainingPlanRepository trainingPlanRepository;

    @Autowired
    private UserRepository userRepository;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setEmail("training@test.com");
        testUser.setName("Training User");
        testUser.setPassword("password");
        testUser = userRepository.save(testUser);
    }

    @Test
    void saveTrainingPlan_ShouldPersistInDatabase() {
        // Given
        TrainingPlan plan = new TrainingPlan();
        plan.setTitle("Push/Pull/Legs");
        plan.setUser(testUser);

        // When
        TrainingPlan saved = trainingPlanRepository.save(plan);

        // Then
        assertNotNull(saved.getId());
        assertEquals("Push/Pull/Legs", saved.getTitle());
        assertEquals(testUser.getId(), saved.getUser().getId());
    }

    @Test
    void findByUser_Email_ShouldReturnUserPlans() {
        // Given
        TrainingPlan plan1 = new TrainingPlan();
        plan1.setTitle("Plan 1");
        plan1.setUser(testUser);

        TrainingPlan plan2 = new TrainingPlan();
        plan2.setTitle("Plan 2");
        plan2.setUser(testUser);

        trainingPlanRepository.save(plan1);
        trainingPlanRepository.save(plan2);

        // When
        List<TrainingPlan> plans = trainingPlanRepository.findByUser_Email("training@test.com");

        // Then
        assertEquals(2, plans.size());
        assertTrue(plans.stream().anyMatch(p -> p.getTitle().equals("Plan 1")));
        assertTrue(plans.stream().anyMatch(p -> p.getTitle().equals("Plan 2")));
    }

    @Test
    void findByUser_Username_ShouldReturnUserPlans() {
        // Given
        testUser.setUsername("trainingUser");
        userRepository.save(testUser);

        TrainingPlan plan = new TrainingPlan();
        plan.setTitle("Username Plan");
        plan.setUser(testUser);
        trainingPlanRepository.save(plan);

        // When
        List<TrainingPlan> plans = trainingPlanRepository.findByUser_Username("trainingUser");

        // Then
        assertEquals(1, plans.size());
        assertEquals("Username Plan", plans.get(0).getTitle());
    }

    @Test
    void deleteTrainingPlan_ShouldRemoveFromDatabase() {
        // Given
        TrainingPlan plan = new TrainingPlan();
        plan.setTitle("Plan to Delete");
        plan.setUser(testUser);
        TrainingPlan saved = trainingPlanRepository.save(plan);

        // When
        trainingPlanRepository.deleteById(saved.getId());

        // Then
        assertFalse(trainingPlanRepository.findById(saved.getId()).isPresent());
    }

    @Test
    void updateTrainingPlan_ShouldPersistChanges() {
        // Given
        TrainingPlan plan = new TrainingPlan();
        plan.setTitle("Original Title");
        plan.setUser(testUser);
        TrainingPlan saved = trainingPlanRepository.save(plan);

        // When
        saved.setTitle("Updated Title");
        TrainingPlan updated = trainingPlanRepository.save(saved);

        // Then
        assertEquals("Updated Title", updated.getTitle());

        // Verify in database
        TrainingPlan found = trainingPlanRepository.findById(saved.getId()).orElse(null);
        assertEquals("Updated Title", found.getTitle());
    }
}