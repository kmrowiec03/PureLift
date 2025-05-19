package com.example.PureLift.entity;

import jakarta.persistence.*;

@Entity
public class ExerciseTemplateMuscle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "exercise_template_id", nullable = false)
    private ExerciseTemplate exerciseTemplate;

    @ManyToOne
    @JoinColumn(name = "muscle_id", nullable = false)
    private Muscle muscle;

    @Column(nullable = false)
    private int intensity; // od 1 do 10

    public Long getId() {
        return id;
    }

    public ExerciseTemplate getExerciseTemplate() {
        return exerciseTemplate;
    }

    public void setExerciseTemplate(ExerciseTemplate exerciseTemplate) {
        this.exerciseTemplate = exerciseTemplate;
    }

    public Muscle getMuscle() {
        return muscle;
    }

    public void setMuscle(Muscle muscle) {
        this.muscle = muscle;
    }

    public int getIntensity() {
        return intensity;
    }

    public void setIntensity(int intensity) {
        this.intensity = intensity;
    }

    public void setId(Long id) {
        this.id = id;
    }
}