package com.example.PureLift.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ExerciseUpdateRequest {
    private Integer sets;
    private Integer reps;
    private Double weight;
}