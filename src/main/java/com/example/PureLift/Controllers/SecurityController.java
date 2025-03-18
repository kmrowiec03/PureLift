package com.example.PureLift.Controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/security")
public class SecurityController {

    private static final Map<Integer, Map<String, String>> users = UserController.getUsersMap();

    private static final Map<String, String> loggedInUsers = new HashMap<>();

    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody Map<String, String> credentials) {
        String email = credentials.get("email");
        String password = credentials.get("password");


        Map.Entry<Integer, Map<String, String>> userEntry = users.entrySet().stream()
                .filter(entry -> email.equals(entry.getValue().get("email")))
                .findFirst()
                .orElse(null);

        if (userEntry == null) {
            return ResponseEntity.status(401).body(Map.of("error", "Invalid email or password"));
        }

        String storedPassword = userEntry.getValue().get("password");


        if (!password.equals(storedPassword)) {
            return ResponseEntity.status(401).body(Map.of("error", "Invalid email or password"));
        }

        loggedInUsers.put(email, userEntry.getValue().get("name"));

        return ResponseEntity.ok(Map.of("message", "Login successful", "name", userEntry.getValue().get("name")));
    }

    @PostMapping("/logout")
    public ResponseEntity<Object> logout(@RequestBody Map<String, String> request) {
        String email = request.get("email");

        if (loggedInUsers.containsKey(email)) {
            loggedInUsers.remove(email);
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
