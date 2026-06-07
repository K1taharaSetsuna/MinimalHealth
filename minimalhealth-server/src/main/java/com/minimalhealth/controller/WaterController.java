package com.minimalhealth.controller;

import com.minimalhealth.model.dto.request.AddWaterRequest;
import com.minimalhealth.model.dto.response.ApiResponse;
import com.minimalhealth.model.dto.response.WaterResponse;
import com.minimalhealth.model.entity.WaterLog;
import com.minimalhealth.repository.WaterLogRepository;
import com.minimalhealth.security.SecurityUtils;
import com.minimalhealth.service.WaterService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("/api/water")
@RequiredArgsConstructor
public class WaterController {

    private final WaterService waterService;
    private final WaterLogRepository waterLogRepository;

    @GetMapping
    public ApiResponse<WaterResponse> getWater(
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
    ) {
        return ApiResponse.success(waterService.getWater(SecurityUtils.getCurrentUserId(), date));
    }

    @PostMapping
    public ApiResponse<WaterResponse> addWater(@Valid @RequestBody AddWaterRequest request) {
        LocalDateTime recordedAt = request.getRecordedAt() != null
            ? LocalDateTime.parse(request.getRecordedAt().replace(" ", "T"))
            : LocalDateTime.now();
        return ApiResponse.success(waterService.addWater(
            SecurityUtils.getCurrentUserId(), request.getAmountMl(), recordedAt
        ));
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Map<String, Boolean>> deleteWater(@PathVariable Long id) {
        waterLogRepository.deleteById(id);
        return ApiResponse.success(Map.of("success", true));
    }

    @GetMapping("/goal")
    public ApiResponse<Map<String, Object>> getGoal(
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
    ) {
        return ApiResponse.success(Map.of(
            "date", date != null ? date.toString() : LocalDate.now().toString(),
            "targetMl", 2500
        ));
    }
}
