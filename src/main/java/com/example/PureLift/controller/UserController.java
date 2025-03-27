package com.example.PureLift.controller;

import com.example.PureLift.entity.User;
import com.example.PureLift.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
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
    public ResponseEntity<Object> getUserById(@PathVariable int id) {

        Optional<User> user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }

    @PostMapping
    public ResponseEntity<Object> addUser(@RequestBody User newUser) {
        if (newUser.getName() == null || newUser.getEmail() == null || newUser.getPassword() == null) {
            return ResponseEntity.status(400).body("Name, email, and password are required");
        }
        User createdUser = userService.addUser(newUser.getName(), newUser.getEmail(), newUser.getPassword());
        return ResponseEntity.status(201).body(createdUser);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteUserById(@PathVariable int id) {
        if (!userService.deleteUserById(id)) {
            return ResponseEntity.status(404).body("User not found");
        }
        return ResponseEntity.status(204).build();
    }
}
