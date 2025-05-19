package com.example.PureLift.entity;
import jakarta.persistence.*;
import java.util.List;

@Entity
public class Muscle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String name;

    @OneToMany(mappedBy = "muscle", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ExerciseTemplateMuscle> usedInExercises;

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

    public List<ExerciseTemplateMuscle> getUsedInExercises() {
        return usedInExercises;
    }

    public void setUsedInExercises(List<ExerciseTemplateMuscle> usedInExercises) {
        this.usedInExercises = usedInExercises;
    }
}