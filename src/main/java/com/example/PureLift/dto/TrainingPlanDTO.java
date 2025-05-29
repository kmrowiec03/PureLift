package com.example.PureLift.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class TrainingPlanDTO {
    private Long id;
    private String title;
    private List<TrainingDayDTO> trainingDays;


}
