package com.example.PureLift.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ExerciseTemplateMuscleDTO {
    private Long id;
    private Long exerciseTemplateId;
    private String exerciseTemplateName;
    private Long muscleId;
    private String muscleName;
    private int intensity;
}
