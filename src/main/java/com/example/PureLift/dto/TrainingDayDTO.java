package com.example.PureLift.dto;

import java.util.List;

public class TrainingDayDTO {
    private Long id;
    private int dayNumber;
    private List<ExerciseDTO> exercises;


    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public int getDayNumber() { return dayNumber; }
    public void setDayNumber(int dayNumber) { this.dayNumber = dayNumber; }

    public List<ExerciseDTO> getExercises() { return exercises; }
    public void setExercises(List<ExerciseDTO> exercises) { this.exercises = exercises; }

}
