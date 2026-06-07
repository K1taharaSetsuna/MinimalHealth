package com.minimalhealth.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class WaterResponse {
    private Integer currentMl;
    private Integer goalMl;
    private Integer percentage;
    private List<WaterLogItem> logs;

    @Data
    @Builder
    @AllArgsConstructor
    public static class WaterLogItem {
        private Long id;
        private Integer amountMl;
        private String recordedAt;
    }
}
