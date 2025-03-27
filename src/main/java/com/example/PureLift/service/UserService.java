package com.example.PureLift.service;

import com.example.PureLift.entity.User;
import com.example.PureLift.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUserById(int id) {
        return userRepository.findById(id);
    }

    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public User addUser(String name, String email, String password) {
        return userRepository.save(new User(0, name, email, password));
    }

    public boolean deleteUserById(int id) {
        return userRepository.deleteById(id);
    }
}
