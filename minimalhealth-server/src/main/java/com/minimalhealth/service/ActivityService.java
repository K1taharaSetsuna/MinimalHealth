package com.minimalhealth.service;

import com.minimalhealth.model.dto.response.DashboardResponse.ActivityItem;
import com.minimalhealth.model.entity.ActivityLog;
import com.minimalhealth.repository.ActivityLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ActivityService {

    private final ActivityLogRepository activityLogRepository;
    private static final DateTimeFormatter TIME_FMT = DateTimeFormatter.ofPattern("HH:mm");

    public List<ActivityItem> getActivities(Long userId, LocalDate dateParam) {
        final LocalDate date = dateParam != null ? dateParam : LocalDate.now();

        List<ActivityLog> logs = activityLogRepository
            .findByUserIdAndRecordDateOrderByOccurredAtDesc(userId, date);

        return logs.stream().map(a -> ActivityItem.builder()
            .id(a.getId()).name(a.getName()).detail(a.getDetail())
            .time(a.getOccurredAt() != null ? a.getOccurredAt().format(TIME_FMT) : "")
            .valueLabel(a.getValueLabel()).activityType(a.getActivityType().name())
            .build()).collect(Collectors.toList());
    }

    public Page<ActivityItem> getActivitiesPaged(Long userId, LocalDate dateParam, int page, int size) {
        final LocalDate date = dateParam != null ? dateParam : LocalDate.now();

        return activityLogRepository
            .findByUserIdAndRecordDateOrderByOccurredAtDesc(userId, date, PageRequest.of(page, size))
            .map(a -> ActivityItem.builder()
                .id(a.getId()).name(a.getName()).detail(a.getDetail())
                .time(a.getOccurredAt() != null ? a.getOccurredAt().format(TIME_FMT) : "")
                .valueLabel(a.getValueLabel()).activityType(a.getActivityType().name())
                .build());
    }
}
