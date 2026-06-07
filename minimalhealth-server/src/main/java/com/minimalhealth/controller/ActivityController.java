package com.minimalhealth.controller;

import com.minimalhealth.model.dto.response.ApiResponse;
import com.minimalhealth.model.dto.response.DashboardResponse.ActivityItem;
import com.minimalhealth.model.entity.ActivityLog;
import com.minimalhealth.model.enums.ActivityType;
import com.minimalhealth.repository.ActivityLogRepository;
import com.minimalhealth.security.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

@RestController
@RequestMapping("/api/timeline")
@RequiredArgsConstructor
public class ActivityController {

    private final ActivityLogRepository activityLogRepository;
    private static final DateTimeFormatter TIME_FMT = DateTimeFormatter.ofPattern("HH:mm");

    @GetMapping
    public ApiResponse<Page<ActivityItem>> getActivities(
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "20") int size
    ) {
        if (date == null) date = LocalDate.now();
        Long userId = SecurityUtils.getCurrentUserId();
        return ApiResponse.success(
            activityLogRepository.findByUserIdAndRecordDateOrderByOccurredAtDesc(userId, date, PageRequest.of(page, size))
                .map(a -> ActivityItem.builder()
                    .id(a.getId()).name(a.getName()).detail(a.getDetail())
                    .time(a.getOccurredAt() != null ? a.getOccurredAt().format(TIME_FMT) : "")
                    .valueLabel(a.getValueLabel()).activityType(a.getActivityType().name())
                    .build())
        );
    }

    @PostMapping
    public ApiResponse<ActivityItem> addActivity(@RequestBody Map<String, Object> body) {
        Long userId = SecurityUtils.getCurrentUserId();
        ActivityLog log = ActivityLog.builder()
            .user(new com.minimalhealth.model.entity.User())
            .activityType(ActivityType.valueOf((String) body.getOrDefault("type", "other")))
            .name((String) body.getOrDefault("name", ""))
            .detail((String) body.getOrDefault("detail", ""))
            .valueLabel((String) body.getOrDefault("valueLabel", ""))
            .valueAmount(body.get("valueAmount") != null ? ((Number) body.get("valueAmount")).doubleValue() : null)
            .valueUnit((String) body.getOrDefault("valueUnit", null))
            .occurredAt(LocalDateTime.now())
            .recordDate(LocalDate.now())
            .build();
        log.getUser().setId(userId);
        log = activityLogRepository.save(log);

        return ApiResponse.success(ActivityItem.builder()
            .id(log.getId()).name(log.getName()).detail(log.getDetail())
            .time(log.getOccurredAt().format(TIME_FMT))
            .valueLabel(log.getValueLabel()).activityType(log.getActivityType().name())
            .build());
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Map<String, Boolean>> deleteActivity(@PathVariable Long id) {
        activityLogRepository.deleteById(id);
        return ApiResponse.success(Map.of("success", true));
    }
}
