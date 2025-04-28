package com.example.PureLift.dto;

import java.util.List;

public class TrainingPlanDTO {
    private Long id;
    private String title;
    private List<TrainingDayDTO> trainingDays;

    // Gettery i Settery
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public List<TrainingDayDTO> getTrainingDays() { return trainingDays; }
    public void setTrainingDays(List<TrainingDayDTO> trainingDays) { this.trainingDays = trainingDays; }


}
