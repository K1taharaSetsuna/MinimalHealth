package com.minimalhealth.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class StartExerciseRequest {
    @NotBlank(message = "运动类型不能为空")
    private String exerciseType;
}
