package com.minimalhealth.controller;

import com.minimalhealth.model.dto.request.AddMealRequest;
import com.minimalhealth.model.dto.response.ApiResponse;
import com.minimalhealth.model.dto.response.DietResponse;
import com.minimalhealth.model.dto.response.DietResponse.MealItem;
import com.minimalhealth.model.entity.Meal;
import com.minimalhealth.repository.MealRepository;
import com.minimalhealth.security.SecurityUtils;
import com.minimalhealth.service.DietService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Map;

@RestController
@RequestMapping("/api/diet")
@RequiredArgsConstructor
public class DietController {

    private final DietService dietService;
    private final MealRepository mealRepository;

    @GetMapping
    public ApiResponse<DietResponse> getDiet(
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
    ) {
        return ApiResponse.success(dietService.getDiet(SecurityUtils.getCurrentUserId(), date));
    }

    @PostMapping
    public ApiResponse<MealItem> addMeal(@Valid @RequestBody AddMealRequest request) {
        LocalDate date = request.getDate() != null ? LocalDate.parse(request.getDate()) : null;
        return ApiResponse.success(dietService.addMeal(
            SecurityUtils.getCurrentUserId(), request.getMealType(),
            request.getFoods(), request.getCalories(), date
        ));
    }

    @PutMapping("/{id}")
    public ApiResponse<MealItem> updateMeal(@PathVariable Long id, @RequestBody Map<String, Object> body) {
        Meal meal = mealRepository.findById(id).orElseThrow();
        if (body.containsKey("foods")) meal.setFoods((String) body.get("foods"));
        if (body.containsKey("calories")) meal.setCalories(((Number) body.get("calories")).intValue());
        mealRepository.save(meal);
        return ApiResponse.success(MealItem.builder()
            .id(meal.getId()).mealType(meal.getMealType().name())
            .calories(meal.getCalories()).foods(meal.getFoods()).recorded(true)
            .build());
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Map<String, Boolean>> deleteMeal(@PathVariable Long id) {
        mealRepository.deleteById(id);
        return ApiResponse.success(Map.of("success", true));
    }

    @GetMapping("/goal")
    public ApiResponse<Map<String, Object>> getGoal(
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
    ) {
        return ApiResponse.success(Map.of("date", date != null ? date.toString() : LocalDate.now().toString(), "targetKcal", 2100));
    }
}
