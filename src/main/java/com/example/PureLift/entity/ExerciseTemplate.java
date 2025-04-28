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
    private String musclesTargeted;

    @OneToMany(mappedBy = "exerciseTemplate")
    private List<Exercise> exercises;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<Exercise> getExercises() {
        return exercises;
    }

    public void setExercises(List<Exercise> exercises) {
        this.exercises = exercises;
    }

    public String getMusclesTargeted() {
        return musclesTargeted;
    }

    public void setMusclesTargeted(String musclesTargeted) {
        this.musclesTargeted = musclesTargeted;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}