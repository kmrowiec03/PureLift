package com.example.PureLift.exception;

public class TrainingPlanNotFoundException extends RuntimeException {
    public TrainingPlanNotFoundException(Long id) {
        super("Training plan with ID " + id + " not found");
    }
}