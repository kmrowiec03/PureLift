package com.example.PureLift.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CoachDTO {
    private Long id;
    private String name;
    private String lastname;
    private String username;
    private String email;
    private String role;
    private boolean enabled;
}
