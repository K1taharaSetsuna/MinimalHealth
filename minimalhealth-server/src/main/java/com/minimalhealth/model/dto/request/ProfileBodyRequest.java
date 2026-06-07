package com.minimalhealth.model.dto.request;

import com.minimalhealth.model.enums.ActivityLevel;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ProfileBodyRequest {
    @NotNull(message = "身高不能为空")
    private Double heightCm;

    @NotNull(message = "体重不能为空")
    private Double weightKg;

    @NotNull(message = "活动水平不能为空")
    private ActivityLevel activityLevel;
}
