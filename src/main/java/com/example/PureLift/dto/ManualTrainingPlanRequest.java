package com.example.PureLift.dto;

import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
public class ManualTrainingPlanRequest {
    private String title;
    private List<ManualTrainingDayRequest> days;
    private String userEmail; // opcjonalne - dla coachów przypisujących plan innemu użytkownikowi
}