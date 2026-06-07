package com.minimalhealth.util;

import com.minimalhealth.model.entity.DailyStats;
import com.minimalhealth.model.entity.HealthGoal;

public class HealthScoreCalculator {

    public static int compute(DailyStats stats, HealthGoal goal) {
        if (stats == null || goal == null) return 0;

        double stepScore = Math.min((double) stats.getSteps() / goal.getDailySteps(), 1.0) * 30.0;
        double waterScore = Math.min((double) stats.getWaterMl() / goal.getDailyWaterMl(), 1.0) * 20.0;

        double sleepGoal = goal.getDailySleepH() > 0 ? goal.getDailySleepH() : 8.0;
        double sleepScore = Math.min(stats.getSleepHours() / sleepGoal, 1.0) * 25.0;

        double hrScore = 25.0;
        int hr = stats.getRestingHr();
        if (hr > 0) {
            if (hr < 50) hrScore = 10.0;
            else if (hr < 60) hrScore = 20.0;
            else if (hr <= 80) hrScore = 25.0;
            else if (hr <= 90) hrScore = 18.0;
            else hrScore = 10.0;
        }

        int total = (int) Math.round(stepScore + waterScore + sleepScore + hrScore);
        return Math.max(0, Math.min(100, total));
    }
}
