package com.example.PureLift.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class TonnageDataDTO {
    private Integer year;
    private Integer week;
    private Double tonnage;
}
