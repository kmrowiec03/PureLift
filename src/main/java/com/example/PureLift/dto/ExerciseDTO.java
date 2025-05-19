package com.example.PureLift.dto;

import java.util.List;

public class ExerciseDTO {
    private Long id;
    private int sets;
    private int reps;
    private double weight;
    private String exerciseName;
    private String description;
    private List<MuscleDTO> musclesTargeted;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public int getSets() { return sets; }
    public void setSets(int sets) { this.sets = sets; }

    public int getReps() { return reps; }
    public void setReps(int reps) { this.reps = reps; }

    public double getWeight() { return weight; }
    public void setWeight(double weight) { this.weight = weight; }

    public String getExerciseName() { return exerciseName; }
    public void setExerciseName(String exerciseName) { this.exerciseName = exerciseName; }

    public List<MuscleDTO> getMusclesTargeted() {
        return musclesTargeted;
    }

    public void setMusclesTargeted(List<MuscleDTO> musclesTargeted) {
        this.musclesTargeted = musclesTargeted;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
