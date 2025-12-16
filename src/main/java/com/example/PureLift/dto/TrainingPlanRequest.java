package com.example.PureLift.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TrainingPlanRequest {
    @NotBlank(message = "Tytuł planu jest wymagany")
    private String title;
    
    @Min(value = 1, message = "Liczba dni musi wynosić minimum 1")
    @Max(value = 4, message = "Liczba dni nie może przekraczać 4")
    private int numberOfDays;
    
    @Min(value = 1, message = "Liczba ćwiczeń na dzień musi wynosić minimum 1")
    @Max(value = 7, message = "Liczba ćwiczeń na dzień nie może przekraczać 7")
    private int exercisesPerDay;
    
    private String planType; // "FULL_BODY" lub "UPPER_LOWER"
    
    @Min(value = 1, message = "Liczba tygodni musi wynosić minimum 1")
    @Max(value = 8, message = "Liczba tygodni nie może przekraczać 8")
    private Integer numberOfWeeks; // liczba tygodni (domyślnie 1)
}
