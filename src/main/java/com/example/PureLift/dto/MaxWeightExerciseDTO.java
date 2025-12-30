package com.example.PureLift.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MaxWeightExerciseDTO {
    private String exerciseName;
    private Double maxWeight1Rep;
    private Double maxWeight3Rep;
    private Double maxWeight5Rep;
    private String exerciseDescription;
    
    public MaxWeightExerciseDTO() {}
    
    public MaxWeightExerciseDTO(String exerciseName, Double maxWeight1Rep, 
                               Double maxWeight3Rep, Double maxWeight5Rep, String exerciseDescription) {
        this.exerciseName = exerciseName;
        this.maxWeight1Rep = maxWeight1Rep;
        this.maxWeight3Rep = maxWeight3Rep;
        this.maxWeight5Rep = maxWeight5Rep;
        this.exerciseDescription = exerciseDescription;
    }
}