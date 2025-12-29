package com.example.PureLift.service;

import com.example.PureLift.dto.RecentExerciseDTO;
import com.example.PureLift.dto.TonnageDataDTO;
import com.example.PureLift.entity.Exercise;
import com.example.PureLift.entity.ExerciseTemplate;
import com.example.PureLift.repository.ExerciseRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.IsoFields;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class MetricsService {
    
    private final ExerciseRepository exerciseRepository;
    
    public MetricsService(ExerciseRepository exerciseRepository) {
        this.exerciseRepository = exerciseRepository;
    }
    
    public List<RecentExerciseDTO> getRecentExercises(Long userId) {
        List<Exercise> userExercises = exerciseRepository.findByTrainingDay_TrainingPlan_User_Id(userId);
        
        // Grupuj po ExerciseTemplate i weź unikalne
        Map<Long, ExerciseTemplate> uniqueTemplates = new HashMap<>();
        for (Exercise exercise : userExercises) {
            ExerciseTemplate template = exercise.getExerciseTemplate();
            if (!uniqueTemplates.containsKey(template.getId())) {
                uniqueTemplates.put(template.getId(), template);
            }
        }
        
        // Konwertuj do DTO
        return uniqueTemplates.values().stream()
                .map(template -> {
                    RecentExerciseDTO dto = new RecentExerciseDTO();
                    dto.setId(template.getId());
                    dto.setName(template.getName());
                    return dto;
                })
                .collect(Collectors.toList());
    }
    
    public List<TonnageDataDTO> getTonnageData(Long userId, Long exerciseId, Integer weeks) {
        List<Exercise> exercises = exerciseRepository.findByTrainingDay_TrainingPlan_User_IdAndExerciseTemplate_Id(userId, exerciseId);
        
        // Grupuj po tygodniach i oblicz tonnage
        Map<String, Double> tonnageByWeek = new HashMap<>();
        
        for (Exercise exercise : exercises) {
            if (exercise.getWeight() != null && exercise.getReps() > 0 && exercise.getSets() > 0) {
                java.util.Date completedDate = exercise.getCompletedDate();
                if (completedDate != null) {
                    LocalDate date = new java.sql.Date(completedDate.getTime()).toLocalDate();
                    int year = date.get(IsoFields.WEEK_BASED_YEAR);
                    int week = date.get(IsoFields.WEEK_OF_WEEK_BASED_YEAR);
                    String key = year + "-" + week;
                    
                    double tonnage = exercise.getWeight() * exercise.getReps() * exercise.getSets();
                    tonnageByWeek.merge(key, tonnage, Double::sum);
                }
            }
        }
        
        // Przygotuj dane dla ostatnich n tygodni
        LocalDate now = LocalDate.now();
        List<TonnageDataDTO> result = new ArrayList<>();
        
        for (int i = weeks - 1; i >= 0; i--) {
            LocalDate targetDate = now.minusWeeks(i);
            int year = targetDate.get(IsoFields.WEEK_BASED_YEAR);
            int week = targetDate.get(IsoFields.WEEK_OF_WEEK_BASED_YEAR);
            String key = year + "-" + week;
            
            Double tonnage = tonnageByWeek.getOrDefault(key, 0.0);
            result.add(new TonnageDataDTO(year, week, tonnage));
        }
        
        return result;
    }
}
