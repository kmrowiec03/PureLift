package com.example.PureLift.service;


import com.example.PureLift.dto.*;
import com.example.PureLift.entity.*;
import com.example.PureLift.repository.ExerciseRepository;
import com.example.PureLift.repository.ExerciseTemplateRepository;
import com.example.PureLift.repository.TrainingDayRepository;
import com.example.PureLift.repository.TrainingPlanRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TrainingService {
    private TrainingPlanRepository trainingPlanRepository;
    private TrainingDayRepository trainingDayRepository;
    private ExerciseTemplateRepository exerciseTemplateRepository;

    public TrainingService(TrainingPlanRepository trainingPlanRepository,
                           TrainingDayRepository trainingDayRepository,
                           ExerciseTemplateRepository exerciseTemplateRepository) {
        this.trainingPlanRepository = trainingPlanRepository;
        this.trainingDayRepository = trainingDayRepository;
        this.exerciseTemplateRepository = exerciseTemplateRepository;
    }
    public TrainingPlan getTrainingPlanById(Long id) {
        return trainingPlanRepository.findById(id).orElse(null);
    }

    public TrainingDay getTrainingDayById(Long planId, Long dayId) {
        return trainingPlanRepository.findById(planId)
                .map(plan -> plan.getTrainingDays().stream()
                        .filter(day -> day.getId().equals(dayId)).findFirst().orElse(null)).orElse(null);
    }

    public List<TrainingPlan> getTrainingPlansByUserName(String userName) {
        return trainingPlanRepository.findByUser_Username(userName);
    }
    public List<TrainingPlan> getTrainingPlansByUserEmail(String email) {
        return trainingPlanRepository.findByUser_Email(email);
    }


    public TrainingPlanDTO convertToDTO(TrainingPlan trainingPlan) {
        TrainingPlanDTO dto = new TrainingPlanDTO();
        dto.setId(trainingPlan.getId());
        dto.setTitle(trainingPlan.getTitle());
        dto.setTrainingDays(
                trainingPlan.getTrainingDays().stream()
                        .map(this::convertDayToDTO)
                        .collect(Collectors.toList())
        );
        return dto;
    }

    private TrainingDayDTO convertDayToDTO(TrainingDay trainingDay) {
        TrainingDayDTO dto = new TrainingDayDTO();
        dto.setId(trainingDay.getId());
        dto.setDayNumber(trainingDay.getDayNumber());
        dto.setExercises(
                trainingDay.getExercises().stream()
                        .map(this::convertExerciseToDTO)
                        .collect(Collectors.toList())
        );
        return dto;
    }
    private ExerciseDTO convertExerciseToDTO(Exercise exercise) {
        ExerciseDTO dto = new ExerciseDTO();
        dto.setId(exercise.getId());
        dto.setSets(exercise.getSets());
        dto.setReps(exercise.getReps());
        dto.setWeight(exercise.getWeight());

        dto.setExerciseName(exercise.getExerciseTemplate().getName());
        dto.setDescription(exercise.getExerciseTemplate().getDescription());

        List<MuscleDTO> muscleDTOs = exercise.getExerciseTemplate().getTargetedMuscles().stream()
                .map(etm -> {
                    MuscleDTO muscleDTO = new MuscleDTO();
                    muscleDTO.setName(etm.getMuscle().getName());
                    muscleDTO.setIntensity(etm.getIntensity());
                    return muscleDTO;
                }).collect(Collectors.toList());

        dto.setMusclesTargeted(muscleDTOs);

        return dto;
    }


    public TrainingPlan generatePlan(User user, TrainingPlanRequest request) {
        List<ExerciseTemplate> allTemplates = exerciseTemplateRepository.findAll();
        if (allTemplates.size() < request.getNumberOfDays() * request.getExercisesPerDay()) {
            throw new IllegalArgumentException("Zbyt mało szablonów ćwiczeń, aby wygenerować plan.");
        }
        Collections.shuffle(allTemplates);

        TrainingPlan plan = new TrainingPlan();
        plan.setTitle(request.getTitle());
        plan.setUser(user);

        List<TrainingDay> days = new ArrayList<>();
        int templateIndex = 0;

        for (int i = 1; i <= request.getNumberOfDays(); i++) {
            TrainingDay day = new TrainingDay();
            day.setDayNumber(i);
            day.setTrainingPlan(plan);

            List<Exercise> exercisesForDay = new ArrayList<>();

            for (int j = 0; j < request.getExercisesPerDay(); j++) {
                ExerciseTemplate template = allTemplates.get(templateIndex++);

                Exercise exercise = new Exercise();
                exercise.setTrainingDay(day);
                exercise.setExerciseTemplate(template);
                exercise.setSets(3);
                exercise.setReps(10);
                exercise.setWeight(0);

                exercisesForDay.add(exercise);
            }

            day.setExercises(exercisesForDay);
            days.add(day);
        }

        plan.setTrainingDays(days);

        return trainingPlanRepository.save(plan);
    }

}
