package com.example.PureLift.service;

import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.Map;

@Service
public class UserService {

    private final Map<Integer, Map<String, String>> users = new HashMap<>() {{
        put(1, Map.of("id", "1", "name", "Jan Kowalski", "email", "jan@example.com", "password", "1234"));
        put(2, Map.of("id", "2", "name", "Anna Nowak", "email", "anna@example.com", "password", "abcd"));
    }};

    public Object getUsers() {
        return users.values();
    }

    public Map<String, String> getUserById(int id) {
        return users.get(id);
    }

    public Map<String, String> addUser(String name, String email, String password) {
        int newId = users.isEmpty() ? 1 : users.keySet().stream().max(Integer::compareTo).orElse(0) + 1;
        Map<String, String> newUser = Map.of(
                "id", String.valueOf(newId),
                "name", name,
                "email", email,
                "password", password
        );
        users.put(newId, newUser);
        return newUser;
    }

    public boolean deleteUserById(int id) {
        return users.remove(id) != null;
    }

    public Map<String, String> getUserByEmail(String email) {
        return users.values().stream()
                .filter(user -> email.equals(user.get("email")))
                .findFirst()
                .orElse(null);
    }

}
