package com.example.PureLift;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;
@RestController
@RequestMapping("/api/users")
public class UserController {
    private static final Map<Integer, Map<String, String>> users = new HashMap<>()
                {{
                put(1, Map.of("id", "1", "name", "Jan Kowalski", "email", "jan@example.com"));
                put(2, Map.of("id", "2", "name", "Anna Nowak", "email", "anna@example.com"));
                }};


    @GetMapping
    public ResponseEntity<Object> getUsers() {
        return ResponseEntity.ok(users.values());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getUserById(@PathVariable int id) {
        if (!users.containsKey(id)) {
            return ResponseEntity.status(404).body(Map.of("error", "User not found"));
        }
        return ResponseEntity.ok(users.get(id));
    }

    @PostMapping
    public ResponseEntity<Object> addUser (@RequestBody Map<String,String> newUser) {
        if(!newUser.containsKey("name") || !newUser.containsKey("email")) {
            return ResponseEntity.status(400).body(Map.of("error", "Username and email are required"));
        }

        int newId = users.isEmpty() ? 1 : users.keySet().stream().max(Integer::compareTo).get()+1;

        users.put(newId, Map.of(
                "id",String.valueOf(newId),
                "name", newUser.get("name"),
                "email", newUser.get("email")
        ));

        return ResponseEntity.status(201).body(users.get(newId));

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteUserById(@PathVariable int id) {
        if (!users.containsKey(id)) {
            return ResponseEntity.status(404).body(Map.of("error", "User not found"));
        }
        users.remove(id);
        return ResponseEntity.status(204).build();
    }


}