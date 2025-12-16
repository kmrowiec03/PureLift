package com.example.PureLift.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class ExerciseDTO {
    private Long id;
    private int sets;
    private int reps;
    private Double weight;
    private String exerciseName;
    private String description;
    private List<MuscleDTO> musclesTargeted;
    private java.util.Date completedDate;

}
