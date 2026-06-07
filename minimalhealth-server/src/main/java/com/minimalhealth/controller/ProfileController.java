package com.minimalhealth.controller;

import com.minimalhealth.model.dto.request.ProfileBasicRequest;
import com.minimalhealth.model.dto.request.ProfileBodyRequest;
import com.minimalhealth.model.dto.request.ProfileGoalsRequest;
import com.minimalhealth.model.dto.response.ApiResponse;
import com.minimalhealth.security.SecurityUtils;
import com.minimalhealth.service.ProfileService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/profile")
@RequiredArgsConstructor
public class ProfileController {

    private final ProfileService profileService;

    @PutMapping("/basic")
    public ApiResponse<Map<String, Object>> updateBasic(@Valid @RequestBody ProfileBasicRequest request) {
        return ApiResponse.success(profileService.updateBasic(SecurityUtils.getCurrentUserId(), request));
    }

    @PutMapping("/body")
    public ApiResponse<Map<String, Object>> updateBody(@Valid @RequestBody ProfileBodyRequest request) {
        return ApiResponse.success(profileService.updateBody(SecurityUtils.getCurrentUserId(), request));
    }

    @PutMapping("/goals")
    public ApiResponse<Map<String, Object>> updateGoals(@RequestBody ProfileGoalsRequest request) {
        return ApiResponse.success(profileService.updateGoals(SecurityUtils.getCurrentUserId(), request));
    }
}
