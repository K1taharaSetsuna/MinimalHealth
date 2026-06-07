package com.minimalhealth.controller;

import com.minimalhealth.model.dto.response.ApiResponse;
import com.minimalhealth.security.SecurityUtils;
import com.minimalhealth.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/profile")
    public ApiResponse<Map<String, Object>> getProfile() {
        return ApiResponse.success(userService.getProfile(SecurityUtils.getCurrentUserId()));
    }

    @PutMapping("/profile")
    public ApiResponse<Map<String, Object>> updateProfile(@RequestBody Map<String, String> body) {
        return ApiResponse.success(userService.updateProfile(
            SecurityUtils.getCurrentUserId(),
            body.get("name"),
            body.get("email")
        ));
    }

    @PutMapping("/body")
    public ApiResponse<Map<String, Object>> updateBodyData(@RequestBody Map<String, Object> body) {
        Double heightCm = body.get("heightCm") != null
            ? ((Number) body.get("heightCm")).doubleValue() : null;
        Double weightKg = body.get("weightKg") != null
            ? ((Number) body.get("weightKg")).doubleValue() : null;
        return ApiResponse.success(userService.updateBodyData(
            SecurityUtils.getCurrentUserId(), heightCm, weightKg
        ));
    }
}
