package com.minimalhealth.service;

import com.minimalhealth.model.dto.response.DashboardResponse;
import com.minimalhealth.model.dto.response.DashboardResponse.ActivityItem;
import com.minimalhealth.model.entity.ActivityLog;
import com.minimalhealth.model.entity.DailyStats;
import com.minimalhealth.model.entity.HealthGoal;
import com.minimalhealth.model.entity.User;
import com.minimalhealth.repository.ActivityLogRepository;
import com.minimalhealth.repository.DailyStatsRepository;
import com.minimalhealth.repository.HealthGoalRepository;
import com.minimalhealth.repository.UserRepository;
import com.minimalhealth.util.GreetingUtils;
import com.minimalhealth.util.HealthScoreCalculator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final DailyStatsRepository dailyStatsRepository;
    private final ActivityLogRepository activityLogRepository;
    private final HealthGoalRepository healthGoalRepository;
    private final UserRepository userRepository;

    private static final DateTimeFormatter TIME_FMT = DateTimeFormatter.ofPattern("HH:mm");

    public DashboardResponse getDashboard(Long userId, LocalDate dateParam) {
        final LocalDate date = dateParam != null ? dateParam : LocalDate.now();

        User user = userRepository.findById(userId).orElse(null);
        DailyStats stats = getOrCreateDailyStats(userId, date);
        HealthGoal goal = healthGoalRepository.findByUserId(userId).orElse(null);

        int healthScore = 0;
        if (stats != null && goal != null) {
            healthScore = HealthScoreCalculator.compute(stats, goal);
        }

        List<ActivityLog> activities = activityLogRepository
            .findByUserIdAndRecordDateOrderByOccurredAtDesc(userId, date);
        List<ActivityItem> activityItems = activities.stream()
            .map(a -> ActivityItem.builder()
                .id(a.getId())
                .name(a.getName())
                .detail(a.getDetail())
                .time(a.getOccurredAt() != null ? a.getOccurredAt().format(TIME_FMT) : "")
                .valueLabel(a.getValueLabel())
                .activityType(a.getActivityType().name())
                .build())
            .collect(Collectors.toList());

        return DashboardResponse.builder()
            .greeting(GreetingUtils.getGreeting(LocalTime.now().getHour()))
            .healthScore(healthScore)
            .stats(DashboardResponse.Stats.builder()
                .steps(stats != null ? stats.getSteps() : 0)
                .restingHr(stats != null ? stats.getRestingHr() : 0)
                .sleepHours(stats != null ? stats.getSleepHours() : 0.0)
                .build())
            .activities(activityItems)
            .build();
    }

    private DailyStats getOrCreateDailyStats(Long userId, LocalDate date) {
        return dailyStatsRepository.findByUserIdAndRecordDate(userId, date)
            .orElseGet(() -> {
                User user = userRepository.findById(userId).orElse(null);
                DailyStats newStats = DailyStats.builder()
                    .user(user)
                    .recordDate(date)
                    .build();
                return dailyStatsRepository.save(newStats);
            });
    }
}
