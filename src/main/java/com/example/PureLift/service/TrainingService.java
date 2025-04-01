package com.example.PureLift.service;


import com.example.PureLift.entity.TrainingDay;
import com.example.PureLift.entity.TrainingPlan;
import com.example.PureLift.repository.TrainingDayRepository;
import com.example.PureLift.repository.TrainingPlanRepository;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public List<TrainingPlan> getAllTrainingPlans() {
        return trainingPlanRepository.findAll();
    }
}
