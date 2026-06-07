package com.minimalhealth.model.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AddWaterRequest {
    @NotNull(message = "水量不能为空")
    @Min(value = 1, message = "水量必须大于0")
    @Max(value = 2000, message = "单次饮水不能超过2000ml")
    private Integer amountMl;

    private String recordedAt; // yyyy-MM-dd HH:mm:ss, default now
}
