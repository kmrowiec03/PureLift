package com.example.PureLift.dto;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
public class CoachRequestDTO {
    private Long id;
    private Long clientId;
    private String clientName;
    private String clientLastname;
    private String clientEmail;
    private Long coachId;
    private String coachName;
    private String message;
    private String status;
    private LocalDateTime createdAt;
}
