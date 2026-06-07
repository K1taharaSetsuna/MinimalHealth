package com.minimalhealth.service;

import com.minimalhealth.model.dto.response.WaterResponse;
import com.minimalhealth.model.dto.response.WaterResponse.WaterLogItem;
import com.minimalhealth.model.entity.*;
import com.minimalhealth.model.enums.ActivityType;
import com.minimalhealth.repository.*;
import com.minimalhealth.util.HealthScoreCalculator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WaterService {

    private final WaterLogRepository waterLogRepository;
    private final DailyStatsRepository dailyStatsRepository;
    private final ActivityLogRepository activityLogRepository;
    private final HealthGoalRepository healthGoalRepository;
    private final UserRepository userRepository;

    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("HH:mm");

    public WaterResponse getWater(Long userId, LocalDate dateParam) {
        final LocalDate date = dateParam != null ? dateParam : LocalDate.now();

        DailyStats stats = dailyStatsRepository.findByUserIdAndRecordDate(userId, date)
            .orElse(DailyStats.builder().build());
        HealthGoal goal = healthGoalRepository.findByUserId(userId)
            .orElse(HealthGoal.builder().dailyWaterMl(2500).build());

        List<WaterLog> logs = waterLogRepository.findByUserIdAndRecordDateOrderByRecordedAtDesc(userId, date);
        List<WaterLogItem> logItems = logs.stream()
            .map(l -> WaterLogItem.builder()
                .id(l.getId())
                .amountMl(l.getAmountMl())
                .recordedAt(l.getRecordedAt().format(FMT))
                .build())
            .collect(Collectors.toList());

        int currentMl = stats.getWaterMl() != null ? stats.getWaterMl() : 0;
        int goalMl = goal.getDailyWaterMl() != null ? goal.getDailyWaterMl() : 2500;
        int percentage = goalMl > 0 ? (int) ((currentMl * 100.0) / goalMl) : 0;

        return WaterResponse.builder()
            .currentMl(currentMl)
            .goalMl(goalMl)
            .percentage(percentage)
            .logs(logItems)
            .build();
    }

    @Transactional
    public WaterResponse addWater(Long userId, int amountMl, LocalDateTime recordedAt) {
        LocalDate today = recordedAt.toLocalDate();

        // Update daily stats
        DailyStats stats = dailyStatsRepository.findByUserIdAndRecordDate(userId, today)
            .orElseGet(() -> {
                User user = userRepository.findById(userId).orElse(null);
                return dailyStatsRepository.save(
                    DailyStats.builder().user(user).recordDate(today).build()
                );
            });
        stats.setWaterMl((stats.getWaterMl() != null ? stats.getWaterMl() : 0) + amountMl);

        // Update health score
        HealthGoal goal = healthGoalRepository.findByUserId(userId).orElse(null);
        if (goal != null) {
            stats.setHealthScore(HealthScoreCalculator.compute(stats, goal));
        }
        dailyStatsRepository.save(stats);

        // Create water log
        WaterLog log = WaterLog.builder()
            .user(userRepository.getReferenceById(userId))
            .amountMl(amountMl)
            .recordDate(today)
            .recordedAt(recordedAt)
            .build();
        waterLogRepository.save(log);

        // Create activity log
        ActivityLog activity = ActivityLog.builder()
            .user(userRepository.getReferenceById(userId))
            .activityType(ActivityType.water)
            .name("饮水")
            .detail(amountMl + "ml · 常温白水")
            .valueLabel(amountMl + "ml")
            .valueAmount((double) amountMl)
            .valueUnit("ml")
            .occurredAt(recordedAt)
            .recordDate(today)
            .build();
        activityLogRepository.save(activity);

        return getWater(userId, today);
    }
}
