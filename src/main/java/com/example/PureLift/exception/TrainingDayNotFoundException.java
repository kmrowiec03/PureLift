package com.example.PureLift.exception;

public class TrainingDayNotFoundException extends RuntimeException {
    public TrainingDayNotFoundException(Long dayId) {
        super("Training day with ID " + dayId + " not found");
    }
}