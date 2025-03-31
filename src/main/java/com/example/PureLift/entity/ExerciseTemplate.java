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

}