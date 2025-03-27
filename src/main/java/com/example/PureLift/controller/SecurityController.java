package com.example.PureLift.controller;

import com.example.PureLift.entity.User;
import com.example.PureLift.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/security")
public class SecurityController {

    private final UserService userService;
    private final Map<String, String> loggedInUsers = new HashMap<>();

    public SecurityController(UserService userService) {
        this.userService = userService;
    }

//    @PostMapping("/login")
//    public ResponseEntity<Object> login(@RequestBody Map<String, String> credentials) {
//        String email = credentials.get("email");
//        String password = credentials.get("password");
//
//        Optional<User> userOptional = userService.getUserByEmail(email);
//
//        if (userOptional.isEmpty() || !password.equals(userOptional.get().getPassword())) {
//            return ResponseEntity.status(401).body(Map.of("error", "Invalid email or password"));
//        }
//
//        User user = userOptional.get();
//        loggedInUsers.put(email, user.getName());
//
//        return ResponseEntity.ok(Map.of("message", "Login successful", "name", user.getName()));
//    }

    @PostMapping("/logout")
    public ResponseEntity<Object> logout(@RequestBody Map<String, String> request) {
        String email = request.get("email");

        if (loggedInUsers.remove(email) != null) {
            return ResponseEntity.ok(Map.of("message", "Logged out successfully"));
        }

        return ResponseEntity.status(400).body(Map.of("error", "User not logged in"));
    }

    @GetMapping("/status")
    public ResponseEntity<Object> getStatus(@RequestParam String email) {
        if (loggedInUsers.containsKey(email)) {
            return ResponseEntity.ok(Map.of("status", "User is logged", "name", loggedInUsers.get(email)));
        }
        return ResponseEntity.status(401).body(Map.of("status", "User not authenticated"));
    }
}
