package com.example.PureLift.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Proste testy jednostkowe dla encji User - testowanie logiki biznesowej bez zależności
 */
class UserTest {

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
    }

    @Test
    void setAndGetId_ShouldWorkCorrectly() {
        // Given
        Long id = 123L;

        // When
        user.setId(id);

        // Then
        assertEquals(id, user.getId());
    }

    @Test
    void setAndGetName_ShouldWorkCorrectly() {
        // Given
        String name = "Jan Kowalski";

        // When
        user.setName(name);

        // Then
        assertEquals(name, user.getName());
    }

    @Test
    void setAndGetEmail_ShouldWorkCorrectly() {
        // Given
        String email = "jan@example.com";

        // When
        user.setEmail(email);

        // Then
        assertEquals(email, user.getEmail());
    }

    @Test
    void setAndGetPassword_ShouldWorkCorrectly() {
        // Given
        String password = "hashedPassword123";

        // When
        user.setPassword(password);

        // Then
        assertEquals(password, user.getPassword());
    }

    @Test
    void newUser_ShouldHaveNullValues() {
        // Given & When
        User newUser = new User();

        // Then
        assertNull(newUser.getId());
        assertNull(newUser.getName());
        assertNull(newUser.getEmail());
        assertNull(newUser.getPassword());
    }
}