package com.minimalhealth.service;

import com.minimalhealth.model.dto.response.DietResponse;
import com.minimalhealth.model.dto.response.DietResponse.MealItem;
import com.minimalhealth.model.entity.*;
import com.minimalhealth.model.enums.ActivityType;
import com.minimalhealth.model.enums.MealType;
import com.minimalhealth.repository.*;
import com.minimalhealth.util.HealthScoreCalculator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DietService {

    private final MealRepository mealRepository;
    private final DailyStatsRepository dailyStatsRepository;
    private final ActivityLogRepository activityLogRepository;
    private final HealthGoalRepository healthGoalRepository;
    private final UserRepository userRepository;

    public DietResponse getDiet(Long userId, LocalDate dateParam) {
        final LocalDate date = dateParam != null ? dateParam : LocalDate.now();

        List<Meal> meals = mealRepository.findByUserIdAndRecordDateOrderByCreatedAtAsc(userId, date);
        int totalCalories = meals.stream().mapToInt(m -> m.getCalories() != null ? m.getCalories() : 0).sum();

        // Build meal items with all 4 types
        Map<MealType, Meal> mealMap = new HashMap<>();
        for (Meal m : meals) mealMap.put(m.getMealType(), m);

        List<MealItem> mealItems = new ArrayList<>();
        for (MealType type : MealType.values()) {
            Meal m = mealMap.get(type);
            if (type == MealType.snack) continue; // skip snack for now
            if (m != null) {
                mealItems.add(MealItem.builder()
                    .id(m.getId()).mealType(m.getMealType().name())
                    .mealTypeLabel(getMealLabel(m.getMealType()))
                    .calories(m.getCalories()).foods(m.getFoods()).recorded(true).build());
            } else {
                mealItems.add(MealItem.builder()
                    .mealType(type.name()).mealTypeLabel(getMealLabel(type))
                    .calories(0).foods(null).recorded(false).build());
            }
        }

        return DietResponse.builder()
            .totalCalories(totalCalories)
            .calorieGoal(2100)
            .meals(mealItems)
            .build();
    }

    @Transactional
    public MealItem addMeal(Long userId, MealType mealType, String foods, int calories, LocalDate dateParam) {
        final LocalDate date = dateParam != null ? dateParam : LocalDate.now();

        Meal meal = Meal.builder()
            .user(userRepository.getReferenceById(userId))
            .mealType(mealType).foods(foods).calories(calories).recordDate(date)
            .build();
        mealRepository.save(meal);

        // Update daily stats
        DailyStats stats = dailyStatsRepository.findByUserIdAndRecordDate(userId, date)
            .orElseGet(() -> dailyStatsRepository.save(
                DailyStats.builder().user(userRepository.getReferenceById(userId)).recordDate(date).build()
            ));
        stats.setCalorieIntake((stats.getCalorieIntake() != null ? stats.getCalorieIntake() : 0) + calories);
        HealthGoal goal = healthGoalRepository.findByUserId(userId).orElse(null);
        if (goal != null) stats.setHealthScore(HealthScoreCalculator.compute(stats, goal));
        dailyStatsRepository.save(stats);

        // Activity log
        activityLogRepository.save(ActivityLog.builder()
            .user(userRepository.getReferenceById(userId))
            .activityType(ActivityType.meal)
            .name(getMealLabel(mealType)).detail(foods)
            .valueLabel(calories + " kcal").valueAmount((double) calories).valueUnit("kcal")
            .occurredAt(LocalDateTime.now()).recordDate(date)
            .build());

        return MealItem.builder()
            .id(meal.getId()).mealType(mealType.name()).mealTypeLabel(getMealLabel(mealType))
            .calories(calories).foods(foods).recorded(true)
            .build();
    }

    private String getMealLabel(MealType type) {
        return switch (type) {
            case breakfast -> "早餐";
            case lunch -> "午餐";
            case dinner -> "晚餐";
            case snack -> "加餐";
        };
    }
}
