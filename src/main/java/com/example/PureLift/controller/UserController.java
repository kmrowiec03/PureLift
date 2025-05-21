package com.example.PureLift.controller;

import com.example.PureLift.entity.User;
import com.example.PureLift.exception.UserNotFoundException;
import com.example.PureLift.exception.UserValidationException;
import com.example.PureLift.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<User>> getUsers() {
        return ResponseEntity.ok(userService.getUsers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(
                userService.getUserById(id)
                        .orElseThrow(() -> new UserNotFoundException(id))
        );
    }

    @PostMapping
    public ResponseEntity<Object> addUser(@RequestBody User newUser) {
        if (newUser.getName() == null || newUser.getEmail() == null || newUser.getPassword() == null) {
            throw new UserValidationException("Name, email, and password are required");
        }
        User createdUser = userService.addUser(newUser);
        return ResponseEntity.status(201).body(createdUser);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteUserById(@PathVariable Long id) {
        if (userService.getUserById(id).isEmpty()) {
            throw new UserNotFoundException(id);
        }
        userService.deleteUserById(id);
        return ResponseEntity.noContent().build();
    }
}
