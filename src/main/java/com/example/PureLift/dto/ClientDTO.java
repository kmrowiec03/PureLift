package com.example.PureLift.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClientDTO {
    private Long id;
    private String name;
    private String lastname;
    private String username;
    private String email;
    private boolean enabled;
}
