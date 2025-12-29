package com.example.PureLift.service;

import com.example.PureLift.dto.UserDTO;
import com.example.PureLift.entity.User;
import com.example.PureLift.repository.UserRepository;
import org.easymock.EasyMock;
import org.easymock.TestSubject;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.easymock.EasyMock.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Testy jednostkowe UserService z EasyMock - sprawdzanie logiki biznesowej
 */
class UserServiceTest {

    private UserRepository mockUserRepository;

    @TestSubject
    private UserService userService;

    private User testUser;

    @BeforeEach
    void setUp() {
        mockUserRepository = createMock(UserRepository.class);
        userService = new UserService(mockUserRepository);
        
        testUser = new User();
        testUser.setId(1L);
        testUser.setName("Jan");
        testUser.setEmail("jan@example.com");
    }

    @AfterEach
    void tearDown() {
        verify(mockUserRepository);
    }

    @Test
    void getUsers_ShouldReturnAllUsers() {
        // Given
        List<User> users = Arrays.asList(testUser);
        expect(mockUserRepository.findAll()).andReturn(users);
        replay(mockUserRepository);

        // When
        List<User> result = userService.getUsers();

        // Then
        assertEquals(1, result.size());
        assertEquals("Jan", result.get(0).getName());
    }

    @Test
    void getUserById_WhenUserExists_ShouldReturnUser() {
        // Given
        expect(mockUserRepository.findById(1L)).andReturn(Optional.of(testUser));
        replay(mockUserRepository);

        // When
        Optional<User> result = userService.getUserById(1L);

        // Then
        assertTrue(result.isPresent());
        assertEquals("jan@example.com", result.get().getEmail());
    }

    @Test
    void getUserById_WhenUserNotExists_ShouldReturnEmpty() {
        // Given
        expect(mockUserRepository.findById(999L)).andReturn(Optional.empty());
        replay(mockUserRepository);

        // When
        Optional<User> result = userService.getUserById(999L);

        // Then
        assertFalse(result.isPresent());
    }

    @Test
    void addUser_ShouldSaveUser() {
        // Given
        User newUser = new User();
        newUser.setEmail("new@example.com");
        expect(mockUserRepository.save(newUser)).andReturn(newUser);
        replay(mockUserRepository);

        // When
        User result = userService.addUser(newUser);

        // Then
        assertEquals("new@example.com", result.getEmail());
    }

    @Test
    void deleteUserById_ShouldCallRepository() {
        // Given
        mockUserRepository.deleteById(1L);
        expectLastCall().once();
        replay(mockUserRepository);

        // When
        userService.deleteUserById(1L);

        // Then - sprawdzane w tearDown przez verify()
    }

    @Test
    void getUserByEmail_WhenUserExists_ShouldReturnUser() {
        // Given
        expect(mockUserRepository.findByEmail("jan@example.com"))
            .andReturn(Optional.of(testUser));
        replay(mockUserRepository);

        // When
        Optional<User> result = userService.getUserByEmail("jan@example.com");

        // Then
        assertTrue(result.isPresent());
        assertEquals("Jan", result.get().getName());
    }
}