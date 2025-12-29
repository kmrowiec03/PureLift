package com.example.PureLift.repository;

import com.example.PureLift.entity.Exercise;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExerciseRepository extends JpaRepository<Exercise, Long> {
    List<Exercise> findByTrainingDay_TrainingPlan_User_Id(Long userId);
    List<Exercise> findByTrainingDay_TrainingPlan_User_IdAndExerciseTemplate_Id(Long userId, Long exerciseTemplateId);
}