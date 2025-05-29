package com.example.PureLift.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TrainingPlanRequest {
    private String title;
    private int numberOfDays;
    private int exercisesPerDay;
}
