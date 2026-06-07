package com.minimalhealth.model.dto.request;

import com.minimalhealth.model.enums.MealType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AddMealRequest {
    @NotNull(message = "餐次不能为空")
    private MealType mealType;

    @NotBlank(message = "食物不能为空")
    private String foods;

    @NotNull(message = "热量不能为空")
    private Integer calories;

    private String date; // yyyy-MM-dd, default today
}
