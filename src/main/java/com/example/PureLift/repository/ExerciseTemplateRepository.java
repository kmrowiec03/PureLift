package com.example.PureLift.repository;

import com.example.PureLift.entity.ExerciseTemplate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExerciseTemplateRepository extends JpaRepository<ExerciseTemplate, Long> {
}