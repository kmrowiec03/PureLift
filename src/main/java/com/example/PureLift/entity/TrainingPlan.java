package com.example.PureLift.entity;


import jakarta.persistence.*;
import java.util.List;

@Entity
public class TrainingPlan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String title;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "trainingPlan", cascade = CascadeType.ALL)
    private List<TrainingDay> trainingDays;


    public List<TrainingDay> getTrainingDays() {
        return trainingDays;
    }

    public User getUser() {
        return user;
    }

    public String getTitle() {
        return title;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setTrainingDays(List<TrainingDay> trainingDays) {
        this.trainingDays = trainingDays;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
