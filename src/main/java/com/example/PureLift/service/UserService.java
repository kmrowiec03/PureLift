package com.example.PureLift.service;

import com.example.PureLift.AppUserRole;
import com.example.PureLift.dto.ClientDTO;
import com.example.PureLift.dto.CoachDTO;
import com.example.PureLift.dto.UserDTO;
import com.example.PureLift.entity.TrainingPlan;
import com.example.PureLift.entity.User;
import com.example.PureLift.exception.UserNotFoundException;
import com.example.PureLift.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }
    public User addUser(User user) {
        return userRepository.save(user);
    }

    public void deleteUserById(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        System.out.println("Szukam użytkownika o emailu: " + email);
        return userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException(email));
    }
    public void updateUserLockStatus(Long id, boolean locked) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
        user.setLocked(locked);
        userRepository.save(user);
    }

    public UserDTO convertToDTO(User user) {
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setLastname(user.getLastname());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setEnabled(user.isEnabled());
        dto.setRole(user.getAppuserRole().name());
        dto.setTrainingPlanTitles(user.getTrainingPlans().stream().map(TrainingPlan::getTitle).collect(Collectors.toList()));
        dto.setLocked(user.isLocked());
        return dto;
    }

    public List<CoachDTO> getAllCoaches() {
        return userRepository.findAll().stream()
                .filter(user -> user.getAppuserRole() == AppUserRole.ROLE_COACH || user.getAppuserRole() == AppUserRole.ROLE_ADMIN)
                .map(this::convertToCoachDTO)
                .collect(Collectors.toList());
    }

    private CoachDTO convertToCoachDTO(User user) {
        CoachDTO dto = new CoachDTO();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setLastname(user.getLastname());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setRole(user.getAppuserRole().name());
        dto.setEnabled(user.isEnabled());
        return dto;
    }

    public List<ClientDTO> getCoachClients(Long coachId) {
        User coach = userRepository.findById(coachId)
                .orElseThrow(() -> new UserNotFoundException(coachId));
        
        if (coach.getClients() == null) {
            return List.of();
        }
        
        return coach.getClients().stream()
                .map(this::convertToClientDTO)
                .collect(Collectors.toList());
    }

    public void assignClientToCoach(Long coachId, Long clientId) {
        User coach = userRepository.findById(coachId)
                .orElseThrow(() -> new UserNotFoundException(coachId));
        User client = userRepository.findById(clientId)
                .orElseThrow(() -> new UserNotFoundException(clientId));
        
        // Sprawdź czy coach ma odpowiednią rolę
        if (coach.getAppuserRole() != AppUserRole.ROLE_COACH && coach.getAppuserRole() != AppUserRole.ROLE_ADMIN) {
            throw new IllegalArgumentException("Użytkownik nie jest trenerem");
        }
        
        client.setCoach(coach);
        userRepository.save(client);
    }

    public void removeClientFromCoach(Long coachId, Long clientId) {
        User client = userRepository.findById(clientId)
                .orElseThrow(() -> new UserNotFoundException(clientId));
        
        if (client.getCoach() == null || !client.getCoach().getId().equals(coachId)) {
            throw new IllegalArgumentException("Klient nie jest przypisany do tego trenera");
        }
        
        client.setCoach(null);
        userRepository.save(client);
    }

    private ClientDTO convertToClientDTO(User user) {
        ClientDTO dto = new ClientDTO();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setLastname(user.getLastname());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setEnabled(user.isEnabled());
        return dto;
    }
}
