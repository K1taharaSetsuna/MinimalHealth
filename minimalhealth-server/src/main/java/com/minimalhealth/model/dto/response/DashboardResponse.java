package com.minimalhealth.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class DashboardResponse {
    private String greeting;
    private Integer healthScore;
    private Stats stats;
    private List<ActivityItem> activities;

    @Data
    @Builder
    @AllArgsConstructor
    public static class Stats {
        private Integer steps;
        private Integer restingHr;
        private Double sleepHours;
    }

    @Data
    @Builder
    @AllArgsConstructor
    public static class ActivityItem {
        private Long id;
        private String name;
        private String detail;
        private String time;
        private String valueLabel;
        private String activityType;
    }
}
