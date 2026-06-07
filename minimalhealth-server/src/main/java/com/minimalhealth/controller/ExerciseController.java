package com.minimalhealth.controller;

import com.minimalhealth.model.dto.request.FinishExerciseRequest;
import com.minimalhealth.model.dto.request.StartExerciseRequest;
import com.minimalhealth.model.dto.response.ApiResponse;
import com.minimalhealth.model.dto.response.ExerciseListResponse;
import com.minimalhealth.model.dto.response.ExerciseListResponse.ExerciseItem;
import com.minimalhealth.repository.ExerciseRecordRepository;
import com.minimalhealth.security.SecurityUtils;
import com.minimalhealth.service.ExerciseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Map;

@RestController
@RequestMapping("/api/exercises")
@RequiredArgsConstructor
public class ExerciseController {

    private final ExerciseService exerciseService;
    private final ExerciseRecordRepository exerciseRecordRepository;

    @GetMapping
    public ApiResponse<ExerciseListResponse> getExercises(
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate weekStart
    ) {
        return ApiResponse.success(exerciseService.getExercises(SecurityUtils.getCurrentUserId(), weekStart));
    }

    @PostMapping("/start")
    public ApiResponse<Map<String, Object>> startExercise(@Valid @RequestBody StartExerciseRequest request) {
        return ApiResponse.success(exerciseService.startExercise(
            SecurityUtils.getCurrentUserId(), request.getExerciseType()
        ));
    }

    @PutMapping("/{id}/finish")
    public ApiResponse<ExerciseItem> finishExercise(
        @PathVariable Long id, @RequestBody FinishExerciseRequest request
    ) {
        return ApiResponse.success(exerciseService.finishExercise(
            SecurityUtils.getCurrentUserId(), id, request
        ));
    }

    @GetMapping("/running/status")
    public ApiResponse<ExerciseItem> getRunningStatus() {
        ExerciseItem status = exerciseService.getRunningStatus(SecurityUtils.getCurrentUserId());
        return ApiResponse.success(status);
    }

    @PostMapping("/checkpoint")
    public ApiResponse<Map<String, Boolean>> checkpoint(@RequestBody Map<String, Object> body) {
        // Save checkpoint data (simplified: just update the exercise record)
        Long exerciseId = body.get("exerciseId") != null ? Long.valueOf(body.get("exerciseId").toString()) : null;
        if (exerciseId != null) {
            exerciseRecordRepository.findById(exerciseId).ifPresent(record -> {
                if (body.get("distanceKm") != null)
                    record.setDistanceKm(((Number) body.get("distanceKm")).doubleValue());
                if (body.get("calories") != null)
                    record.setCalories(((Number) body.get("calories")).intValue());
                if (body.get("heartRate") != null)
                    record.setAvgHr(((Number) body.get("heartRate")).intValue());
                exerciseRecordRepository.save(record);
            });
        }
        return ApiResponse.success(Map.of("success", true));
    }

    @PostMapping("/finish")
    public ApiResponse<ExerciseItem> finishExerciseDirect(@RequestBody Map<String, Object> body) {
        Long exerciseId = body.get("exerciseId") != null ? Long.valueOf(body.get("exerciseId").toString()) : null;
        if (exerciseId == null) return ApiResponse.error(400, "缺少exerciseId");
        FinishExerciseRequest req = new FinishExerciseRequest();
        if (body.get("durationMin") != null) req.setDurationMin(((Number) body.get("durationMin")).intValue());
        if (body.get("totalSeconds") != null) {
            int secs = ((Number) body.get("totalSeconds")).intValue();
            req.setDurationMin(Math.max(1, secs / 60)); // minimum 1 minute
        }
        if (body.get("distanceKm") != null) req.setDistanceKm(((Number) body.get("distanceKm")).doubleValue());
        if (body.get("calories") != null) req.setCalories(((Number) body.get("calories")).intValue());
        if (body.get("avgHeartRate") != null) req.setAvgHr(((Number) body.get("avgHeartRate")).intValue());
        return ApiResponse.success(exerciseService.finishExercise(SecurityUtils.getCurrentUserId(), exerciseId, req));
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Map<String, Boolean>> deleteExercise(@PathVariable Long id) {
        exerciseRecordRepository.deleteById(id);
        return ApiResponse.success(Map.of("success", true));
    }
}
