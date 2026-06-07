package com.minimalhealth.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class DietResponse {
    private Integer totalCalories;
    private Integer calorieGoal;
    private List<MealItem> meals;

    @Data
    @Builder
    @AllArgsConstructor
    public static class MealItem {
        private Long id;
        private String mealType;
        private String mealTypeLabel;
        private Integer calories;
        private String foods;
        private Boolean recorded;
    }
}
