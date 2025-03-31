package com.example.PureLift.entity;

import jakarta.persistence.*;

@Entity
public class Exercise {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int sets;
    private int reps;
    private double weight;

    @ManyToOne
    @JoinColumn(name = "exercise_template_id", nullable = false)
    private ExerciseTemplate exerciseTemplate;

    @ManyToOne
    @JoinColumn(name = "training_day_id", nullable = false)
    private TrainingDay trainingDay;

}
