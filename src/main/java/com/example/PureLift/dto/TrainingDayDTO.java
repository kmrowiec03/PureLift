package com.example.PureLift.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class TrainingDayDTO {
    private Long id;
    private int dayNumber;
    private List<ExerciseDTO> exercises;


}
