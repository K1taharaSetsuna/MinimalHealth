package com.minimalhealth.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class ExerciseListResponse {
    private WeeklyStats weeklyStats;
    private List<ExerciseItem> records;

    @Data
    @Builder
    @AllArgsConstructor
    public static class WeeklyStats {
        private Integer count;
        private Double totalDistanceKm;
        private Integer totalCalories;
    }

    @Data
    @Builder
    @AllArgsConstructor
    public static class ExerciseItem {
        private Long id;
        private String exerciseType;
        private Integer durationMin;
        private Double distanceKm;
        private Integer calories;
        private String avgPace;
        private String routeDesc;
        private String recordDate;
        private String status;
    }
}
