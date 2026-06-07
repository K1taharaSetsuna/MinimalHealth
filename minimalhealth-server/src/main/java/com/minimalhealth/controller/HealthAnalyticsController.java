package com.minimalhealth.controller;

import com.minimalhealth.model.dto.response.ApiResponse;
import com.minimalhealth.security.SecurityUtils;
import com.minimalhealth.service.HealthAnalyticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/analytics")
@RequiredArgsConstructor
public class HealthAnalyticsController {

    private final HealthAnalyticsService analyticsService;

    @GetMapping
    public ApiResponse<Map<String, Object>> getAnalytics(
        @RequestParam(defaultValue = "week") String period
    ) {
        return ApiResponse.success(analyticsService.getAnalytics(SecurityUtils.getCurrentUserId(), period));
    }
}
