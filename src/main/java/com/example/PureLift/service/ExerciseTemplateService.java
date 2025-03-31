package com.example.PureLift.service;

import com.example.PureLift.entity.ExerciseTemplate;
import com.example.PureLift.repository.ExerciseTemplateRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExerciseTemplateService  {
    private final ExerciseTemplateRepository repository;

    public ExerciseTemplateService(ExerciseTemplateRepository repository) {
        this.repository = repository;
    }

    public List<ExerciseTemplate> getAllExercises() {
        return repository.findAll();
    }

    public ExerciseTemplate createExerciseTemplate(ExerciseTemplate exerciseTemplate) {
        return repository.save(exerciseTemplate);
    }
}
