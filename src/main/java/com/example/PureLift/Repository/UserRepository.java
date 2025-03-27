package com.example.PureLift.repository;

import com.example.PureLift.entity.User;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class UserRepository {
    private final Map<Integer, User> users = new HashMap<>();

    public UserRepository() {
        users.put(1, new User(1, "Jan Kowalski", "jan@example.com", "1234"));
        users.put(2, new User(2, "Anna Nowak", "anna@example.com", "abcd"));
    }

    public List<User> findAll() {
        return new ArrayList<>(users.values());
    }

    public Optional<User> findById(int id) {
        return Optional.ofNullable(users.get(id));
    }

    public Optional<User> findByEmail(String email) {
        return users.values().stream()
                .filter(user -> user.getEmail().equals(email))
                .findFirst();
    }

    public User save(User user) {
        int newId = users.keySet().stream().max(Integer::compareTo).orElse(0) + 1;
        user.setId(newId);
        users.put(newId, user);
        return user;
    }

    public boolean deleteById(int id) {
        return users.remove(id) != null;
    }
}
