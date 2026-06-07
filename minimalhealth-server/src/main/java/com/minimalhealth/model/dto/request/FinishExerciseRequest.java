package com.minimalhealth.model.dto.request;

import lombok.Data;

@Data
public class FinishExerciseRequest {
    private Integer durationMin;
    private Double distanceKm;
    private Integer calories;
    private String avgPace;
    private Integer avgHr;
    private String routeDesc;
}
