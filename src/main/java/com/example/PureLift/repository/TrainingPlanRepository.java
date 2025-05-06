package com.example.PureLift.repository;


import com.example.PureLift.entity.TrainingPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TrainingPlanRepository extends JpaRepository<TrainingPlan, Long> {
    List<TrainingPlan> findByUser_Username(String username);

    List<TrainingPlan> findByUser_Email(String email);
}
