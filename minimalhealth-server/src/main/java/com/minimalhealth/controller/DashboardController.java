package com.minimalhealth.controller;

import com.minimalhealth.model.dto.response.ApiResponse;
import com.minimalhealth.model.dto.response.DashboardResponse;
import com.minimalhealth.security.SecurityUtils;
import com.minimalhealth.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping
    public ApiResponse<DashboardResponse> getDashboard(
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
    ) {
        return ApiResponse.success(dashboardService.getDashboard(SecurityUtils.getCurrentUserId(), date));
    }
}
