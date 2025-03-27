package com.example.PureLift.controller;

import com.example.PureLift.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<Object> getUsers() {
        return ResponseEntity.ok(userService.getUsers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getUserById(@PathVariable int id) {
        Map<String, String> user = userService.getUserById(id);
        if (user == null) {
            return ResponseEntity.status(404).body(Map.of("error", "User not found"));
        }
        return ResponseEntity.ok(user);
    }

    @PostMapping
    public ResponseEntity<Object> addUser(@RequestBody Map<String, String> newUser) {
        if (!newUser.containsKey("name") || !newUser.containsKey("email") || !newUser.containsKey("password")) {
            return ResponseEntity.status(400).body(Map.of("error", "Name, email, and password are required"));
        }
        return ResponseEntity.status(201).body(userService.addUser(
                newUser.get("name"), newUser.get("email"), newUser.get("password")
        ));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteUserById(@PathVariable int id) {
        if (!userService.deleteUserById(id)) {
            return ResponseEntity.status(404).body(Map.of("error", "User not found"));
        }
        return ResponseEntity.status(204).build();
    }
}
