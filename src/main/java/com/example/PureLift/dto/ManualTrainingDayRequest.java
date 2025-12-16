package com.example.PureLift.dto;

import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
public class ManualTrainingDayRequest {
    private Long id;
    private int dayNumber;
    private List<ManualExerciseRequest> exercises;
}