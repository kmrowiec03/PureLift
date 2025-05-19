package com.example.PureLift.service;


import com.example.PureLift.dto.ExerciseDTO;
import com.example.PureLift.dto.MuscleDTO;
import com.example.PureLift.dto.TrainingDayDTO;
import com.example.PureLift.dto.TrainingPlanDTO;
import com.example.PureLift.entity.Exercise;
import com.example.PureLift.entity.TrainingDay;
import com.example.PureLift.entity.TrainingPlan;
import com.example.PureLift.repository.TrainingDayRepository;
import com.example.PureLift.repository.TrainingPlanRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TrainingService {
    private TrainingPlanRepository trainingPlanRepository;
    private TrainingDayRepository trainingDayRepository;

    public TrainingService(TrainingPlanRepository trainingPlanRepository, TrainingDayRepository trainingDayRepository) {
        this.trainingPlanRepository = trainingPlanRepository;
        this.trainingDayRepository = trainingDayRepository;
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



}
