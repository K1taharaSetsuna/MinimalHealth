package com.minimalhealth.service;

import com.minimalhealth.model.entity.DailyStats;
import com.minimalhealth.model.entity.HealthGoal;
import com.minimalhealth.repository.DailyStatsRepository;
import com.minimalhealth.repository.HealthGoalRepository;
import com.minimalhealth.util.HealthScoreCalculator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Service
@RequiredArgsConstructor
public class HealthAnalyticsService {

    private final DailyStatsRepository dailyStatsRepository;
    private final HealthGoalRepository healthGoalRepository;

    public Map<String, Object> getAnalytics(Long userId, String period) {
        LocalDate now = LocalDate.now();
        LocalDate start, prevStart, prevEnd;

        if ("month".equals(period)) {
            start = now.withDayOfMonth(1);
            prevStart = now.minusMonths(1).withDayOfMonth(1);
            prevEnd = start.minusDays(1);
        } else {
            // week
            start = now.with(java.time.DayOfWeek.MONDAY);
            prevStart = start.minusWeeks(1);
            prevEnd = start.minusDays(1);
        }

        HealthGoal goal = healthGoalRepository.findByUserId(userId).orElse(null);

        // Current period stats
        List<DailyStats> currentStats = dailyStatsRepository
            .findByUserIdAndRecordDateBetween(userId, start, now);
        int currentScore = computeAvgScore(currentStats, goal);

        // Previous period stats
        List<DailyStats> prevStats = dailyStatsRepository
            .findByUserIdAndRecordDateBetween(userId, prevStart, prevEnd);
        int prevScore = computeAvgScore(prevStats, goal);

        int scoreChange = currentScore - prevScore;

        // Average metrics
        double avgSleep = currentStats.stream().mapToDouble(s -> s.getSleepHours() != null ? s.getSleepHours() : 0).average().orElse(0);
        double avgSteps = currentStats.stream().mapToInt(s -> s.getSteps() != null ? s.getSteps() : 0).average().orElse(0);
        double avgHr = currentStats.stream().mapToInt(s -> s.getRestingHr() != null ? s.getRestingHr() : 0).filter(v -> v > 0).average().orElse(0);
        double avgWater = currentStats.stream().mapToInt(s -> s.getWaterMl() != null ? s.getWaterMl() : 0).average().orElse(0);

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("overallScore", currentScore);
        result.put("scoreChange", scoreChange);
        result.put("period", period);

        List<Map<String, Object>> metrics = new ArrayList<>();
        metrics.add(metricItem("平均睡眠", String.format("%.1fh", avgSleep)));
        metrics.add(metricItem("日均步数", String.format("%,.0f", avgSteps)));
        metrics.add(metricItem("静息心率", avgHr > 0 ? String.format("%.0f bpm", avgHr) : "--"));
        metrics.add(metricItem("日均饮水", String.format("%.1fL", avgWater / 1000)));
        result.put("metrics", metrics);

        return result;
    }

    private int computeAvgScore(List<DailyStats> statsList, HealthGoal goal) {
        if (statsList.isEmpty() || goal == null) return 0;
        return (int) Math.round(statsList.stream()
            .mapToInt(s -> HealthScoreCalculator.compute(s, goal))
            .average().orElse(0));
    }

    private Map<String, Object> metricItem(String label, String value) {
        Map<String, Object> m = new LinkedHashMap<>();
        m.put("label", label);
        m.put("value", value);
        return m;
    }
}
