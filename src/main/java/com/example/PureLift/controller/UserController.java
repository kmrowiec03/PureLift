package com.example.PureLift.controller;

import com.example.PureLift.dto.UserDTO;
import com.example.PureLift.entity.User;
import com.example.PureLift.exception.UserNotFoundException;
import com.example.PureLift.exception.UserValidationException;
import com.example.PureLift.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // Zwraca listę użytkowników jako DTO
    @GetMapping
    public ResponseEntity<List<UserDTO>> getUsers() {
        List<UserDTO> users = userService.getUsers().stream()
                .map(userService::convertToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(users);
    }

    // Zwraca dane zalogowanego użytkownika jako DTO
    @GetMapping("/me")
    public ResponseEntity<UserDTO> getCurrentUser() {
        User userToId = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.getUserByEmail(email)
                .orElseThrow(() -> new UserNotFoundException(userToId.getId()));
        return ResponseEntity.ok(userService.convertToDTO(user));
    }

    // Zwraca konkretnego użytkownika jako DTO
    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long id) {
        User user = userService.getUserById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
        return ResponseEntity.ok(userService.convertToDTO(user));
    }

    @PostMapping
    public ResponseEntity<UserDTO> addUser(@RequestBody User newUser) {
        if (newUser.getName() == null || newUser.getEmail() == null || newUser.getPassword() == null) {
            throw new UserValidationException("Name, email, and password are required");
        }
        User createdUser = userService.addUser(newUser);
        return ResponseEntity.status(201).body(userService.convertToDTO(createdUser));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUserById(@PathVariable Long id) {
        if (userService.getUserById(id).isEmpty()) {
            throw new UserNotFoundException(id);
        }
        userService.deleteUserById(id);
        return ResponseEntity.noContent().build();
    }
    @PatchMapping("/{id}/lock")
    public ResponseEntity<Void> toggleUserLockStatus( @PathVariable Long id, @RequestBody Map<String, Boolean> body) {
        boolean locked = body.get("locked");
        userService.updateUserLockStatus(id, locked);
        return ResponseEntity.ok().build();
    }
}
