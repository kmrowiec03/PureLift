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


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TrainingDay getTrainingDay() {
        return trainingDay;
    }

    public void setTrainingDay(TrainingDay trainingDay) {
        this.trainingDay = trainingDay;
    }

    public ExerciseTemplate getExerciseTemplate() {
        return exerciseTemplate;
    }

    public void setExerciseTemplate(ExerciseTemplate exerciseTemplate) {
        this.exerciseTemplate = exerciseTemplate;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public int getReps() {
        return reps;
    }

    public void setReps(int reps) {
        this.reps = reps;
    }

    public int getSets() {
        return sets;
    }

    public void setSets(int sets) {
        this.sets = sets;
    }
}
