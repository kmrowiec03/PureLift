package com.example.PureLift.entity;

import jakarta.persistence.*;
import java.util.List;

@Entity
public class ExerciseTemplate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;

    @OneToMany(mappedBy = "exerciseTemplate", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ExerciseTemplateMuscle> targetedMuscles;

    @OneToMany(mappedBy = "exerciseTemplate")
    private List<Exercise> exercises;

    // Gettery i settery

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<ExerciseTemplateMuscle> getTargetedMuscles() {
        return targetedMuscles;
    }

    public void setTargetedMuscles(List<ExerciseTemplateMuscle> targetedMuscles) {
        this.targetedMuscles = targetedMuscles;
    }

    public List<Exercise> getExercises() {
        return exercises;
    }

    public void setExercises(List<Exercise> exercises) {
        this.exercises = exercises;
    }
}
