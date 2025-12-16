package com.example.PureLift.dto;

import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
public class ManualExerciseRequest {
    private Long id;
    private String templateId;
    private String name;
    private String description;
    private int series;
    private int reps;
    private List<Object> muscles; // ignorujemy na razie, używamy templateId
}