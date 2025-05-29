package com.example.PureLift.dto;


import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class UserDTO {
    private Long id;
    private String name;
    private String lastname;
    private String username;
    private String email;
    private String role;
    private boolean enabled;
    private boolean locked;
    private List<String> trainingPlanTitles;

}
