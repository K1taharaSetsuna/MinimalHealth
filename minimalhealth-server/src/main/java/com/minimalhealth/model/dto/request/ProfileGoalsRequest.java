package com.minimalhealth.model.dto.request;

import lombok.Data;

@Data
public class ProfileGoalsRequest {
    private Integer dailySteps = 8000;
    private Double dailySleepH = 8.0;
    private Integer dailyWaterMl = 2500;
}
