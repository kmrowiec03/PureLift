package com.example.PureLift.entity;


import jakarta.persistence.*;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

@Entity
public class TrainingDay {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int dayNumber;

    @ManyToOne
    @JoinColumn(name = "training_plan_id", nullable = false)
    private TrainingPlan trainingPlan;

    @OneToMany(mappedBy = "trainingDay", cascade = CascadeType.ALL)
    private List<Exercise> exercises;

    public Long getId() {
        return id;
    }
}
