package com.example.PureLift.integration;

import com.example.PureLift.entity.User;
import com.example.PureLift.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testy integracyjne UserRepository z bazą danych H2
 * Testowanie operacji bazodanowych bez mockowania
 */
@DataJpaTest
@ActiveProfiles("test")
class UserRepositoryIntegrationTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void saveUser_ShouldPersistInDatabase() {
        // Given
        User user = new User();
        user.setEmail("test@example.com");
        user.setName("Test");
        user.setPassword("password123");

        // When
        User saved = userRepository.save(user);

        // Then
        assertNotNull(saved.getId());
        assertEquals("test@example.com", saved.getEmail());
        assertEquals("Test", saved.getName());
    }

    @Test
    void findByEmail_ShouldReturnUser() {
        // Given
        User user = new User();
        user.setEmail("find@example.com");
        user.setName("Find User");
        user.setPassword("password");
        userRepository.save(user);

        // When
        var found = userRepository.findByEmail("find@example.com");

        // Then
        assertTrue(found.isPresent());
        assertEquals("Find User", found.get().getName());
    }

    @Test
    void deleteById_ShouldRemoveUser() {
        // Given
        User user = new User();
        user.setEmail("delete@example.com");
        user.setName("Delete User");
        user.setPassword("password");
        User saved = userRepository.save(user);

        // When
        userRepository.deleteById(saved.getId());

        // Then
        assertFalse(userRepository.findById(saved.getId()).isPresent());
    }

    @Test
    void findAll_ShouldReturnAllUsers() {
        // Given
        User user1 = new User();
        user1.setEmail("user1@example.com");
        user1.setName("User 1");
        user1.setPassword("password");
        
        User user2 = new User();
        user2.setEmail("user2@example.com");
        user2.setName("User 2");
        user2.setPassword("password");
        
        userRepository.save(user1);
        userRepository.save(user2);

        // When
        var users = userRepository.findAll();

        // Then
        assertTrue(users.size() >= 2);
        assertTrue(users.stream().anyMatch(u -> u.getEmail().equals("user1@example.com")));
        assertTrue(users.stream().anyMatch(u -> u.getEmail().equals("user2@example.com")));
    }
}