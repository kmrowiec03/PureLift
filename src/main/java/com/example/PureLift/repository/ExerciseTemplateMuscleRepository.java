package com.example.PureLift.repository;

import com.example.PureLift.entity.ExerciseTemplateMuscle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExerciseTemplateMuscleRepository extends JpaRepository<ExerciseTemplateMuscle, Long> {
}
