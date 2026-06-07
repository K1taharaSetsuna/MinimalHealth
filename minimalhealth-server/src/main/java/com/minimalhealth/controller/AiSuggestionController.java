package com.minimalhealth.controller;

import com.minimalhealth.model.dto.response.ApiResponse;
import com.minimalhealth.security.SecurityUtils;
import com.minimalhealth.service.AiSuggestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Map;

@RestController
@RequestMapping("/api/ai")
@RequiredArgsConstructor
public class AiSuggestionController {

    private final AiSuggestionService aiSuggestionService;

    @GetMapping("/suggestions")
    public ApiResponse<Map<String, Object>> getSuggestions(
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
    ) {
        return ApiResponse.success(aiSuggestionService.getSuggestions(SecurityUtils.getCurrentUserId(), date));
    }
}
