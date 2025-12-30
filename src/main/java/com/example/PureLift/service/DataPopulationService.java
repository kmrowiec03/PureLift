package com.example.PureLift.service;

import com.example.PureLift.AppUserRole;
import com.example.PureLift.entity.Exercise;
import com.example.PureLift.entity.User;
import com.example.PureLift.repository.ExerciseRepository;
import com.example.PureLift.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Random;

@Service
public class DataPopulationService {
    
    private final ExerciseRepository exerciseRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final Random random = new Random();
    
    public DataPopulationService(ExerciseRepository exerciseRepository, 
                               UserRepository userRepository, 
                               PasswordEncoder passwordEncoder) {
        this.exerciseRepository = exerciseRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }
    
    @Transactional
    public void populateExerciseData() {
        List<Exercise> exercises = exerciseRepository.findAll();
        
        for (Exercise exercise : exercises) {
            // Losowy ciężar między 20-50 kg
            double weight = 20.0 + (random.nextDouble() * 30.0); // 20-50 kg
            weight = Math.round(weight * 2.0) / 2.0; // Zaokrąglenie do 0.5 kg
            
            // Losowa liczba powtórzeń między 2-7
            int reps = 2 + random.nextInt(6); // 2-7 powtórzeń
            
            // Losowa data z ostatnich 7 tygodni (49 dni)
            LocalDate randomDate = LocalDate.now().minusDays(random.nextInt(49));
            Date completedDate = Date.from(randomDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
            
            // Aktualizacja danych
            exercise.setWeight(weight);
            exercise.setReps(reps);
            exercise.setCompletedDate(completedDate);
            
            exerciseRepository.save(exercise);
        }
    }
    
    public String getPopulationSummary() {
        List<Exercise> exercises = exerciseRepository.findAll();
        long exercisesWithWeight = exercises.stream()
            .filter(ex -> ex.getWeight() != null && ex.getWeight() > 0)
            .count();
        
        return String.format("Zaktualizowano %d ćwiczeń z %d dostępnych. " +
                "Zakres ciężarów: 20-50kg, powtórzeń: 2-7, daty: ostatnie 7 tygodni.",
                exercisesWithWeight, exercises.size());
    }
    
    @Transactional
    public void createTestUsers() {
        // Sprawdź czy użytkownicy już istnieją
        if (userRepository.findByEmail("admin@purelift.com").isPresent()) {
            return; // Użytkownicy już istnieją
        }
        
        // Hasło: "password123"
        String encodedPassword = passwordEncoder.encode("password123");
        
        // 1. Admin
        User admin = User.builder()
            .name("Admin")
            .lastname("System")
            .username("admin")
            .email("admin@purelift.com")
            .password(encodedPassword)
            .appuserRole(AppUserRole.ROLE_ADMIN)
            .enabled(true)
            .locked(false)
            .build();
        userRepository.save(admin);
        
        // 2. Trener 1
        User coach1 = User.builder()
            .name("Jan")
            .lastname("Kowalski")
            .username("jan.kowalski")
            .email("jan.kowalski@purelift.com")
            .password(encodedPassword)
            .appuserRole(AppUserRole.ROLE_COACH)
            .enabled(true)
            .locked(false)
            .build();
        userRepository.save(coach1);
        
        // 3. Trener 2
        User coach2 = User.builder()
            .name("Anna")
            .lastname("Nowak")
            .username("anna.nowak")
            .email("anna.nowak@purelift.com")
            .password(encodedPassword)
            .appuserRole(AppUserRole.ROLE_COACH)
            .enabled(true)
            .locked(false)
            .build();
        userRepository.save(coach2);
        
        // 4. Trener 3
        User coach3 = User.builder()
            .name("Piotr")
            .lastname("Wiśniewski")
            .username("piotr.wisniewski")
            .email("piotr.wisniewski@purelift.com")
            .password(encodedPassword)
            .appuserRole(AppUserRole.ROLE_COACH)
            .enabled(true)
            .locked(false)
            .build();
        userRepository.save(coach3);
        
        // 5. Zwykły użytkownik
        User user = User.builder()
            .name("Tomasz")
            .lastname("Zieliński")
            .username("tomasz.zielinski")
            .email("tomasz.zielinski@purelift.com")
            .password(encodedPassword)
            .appuserRole(AppUserRole.ROLE_USER)
            .enabled(true)
            .locked(false)
            .build();
        userRepository.save(user);
    }
    
    public String getUserCreationSummary() {
        long totalUsers = userRepository.count();
        long coaches = userRepository.countByAppuserRole(AppUserRole.ROLE_COACH);
        long users = userRepository.countByAppuserRole(AppUserRole.ROLE_USER);
        long admins = userRepository.countByAppuserRole(AppUserRole.ROLE_ADMIN);
        
        return String.format("Stworzono użytkowników: %d (Trenerzy: %d, Użytkownicy: %d, Administratorzy: %d). " +
                "Hasło dla wszystkich: 'password123'",
                totalUsers, coaches, users, admins);
    }
}