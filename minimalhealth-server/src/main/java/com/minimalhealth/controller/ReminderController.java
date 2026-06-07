package com.minimalhealth.controller;

import com.minimalhealth.model.dto.request.ReminderUpdateRequest;
import com.minimalhealth.model.dto.response.ApiResponse;
import com.minimalhealth.security.SecurityUtils;
import com.minimalhealth.service.ReminderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/reminders")
@RequiredArgsConstructor
public class ReminderController {

    private final ReminderService reminderService;

    @GetMapping
    public ApiResponse<List<Map<String, Object>>> getReminders() {
        return ApiResponse.success(reminderService.getReminders(SecurityUtils.getCurrentUserId()));
    }

    @PutMapping
    public ApiResponse<List<Map<String, Object>>> updateReminders(@RequestBody ReminderUpdateRequest request) {
        return ApiResponse.success(reminderService.updateReminders(SecurityUtils.getCurrentUserId(), request));
    }
}
