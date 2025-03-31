package com.example.PureLift.repository;

import com.example.PureLift.entity.TrainingDay;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TrainingDayRepository extends JpaRepository<TrainingDay, Long> {
}