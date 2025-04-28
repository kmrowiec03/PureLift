package com.example.PureLift.dto;

public class ExerciseDTO {
    private Long id;
    private int sets;
    private int reps;
    private double weight;
    private String exerciseName;

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
}
