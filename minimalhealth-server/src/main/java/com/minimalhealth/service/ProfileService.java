package com.minimalhealth.service;

import com.minimalhealth.exception.BusinessException;
import com.minimalhealth.model.dto.request.ProfileBasicRequest;
import com.minimalhealth.model.dto.request.ProfileBodyRequest;
import com.minimalhealth.model.dto.request.ProfileGoalsRequest;
import com.minimalhealth.model.entity.HealthGoal;
import com.minimalhealth.model.entity.User;
import com.minimalhealth.repository.HealthGoalRepository;
import com.minimalhealth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ProfileService {

    private final UserRepository userRepository;
    private final HealthGoalRepository healthGoalRepository;

    public Map<String, Object> updateBasic(Long userId, ProfileBasicRequest request) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new BusinessException("用户不存在"));

        user.setName(request.getName());
        user.setGender(request.getGender());
        user.setBirthDate(LocalDate.parse(request.getBirthDate()));
        userRepository.save(user);

        return Map.of("profileComplete", user.getProfileComplete(), "currentStep", 1);
    }

    public Map<String, Object> updateBody(Long userId, ProfileBodyRequest request) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new BusinessException("用户不存在"));

        user.setHeightCm(request.getHeightCm());
        user.setWeightKg(request.getWeightKg());
        user.setActivityLevel(request.getActivityLevel());
        userRepository.save(user);

        return Map.of("profileComplete", user.getProfileComplete(), "currentStep", 2);
    }

    public Map<String, Object> updateGoals(Long userId, ProfileGoalsRequest request) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new BusinessException("用户不存在"));

        HealthGoal goal = healthGoalRepository.findByUserId(userId)
            .orElseGet(() -> HealthGoal.builder().user(user).build());

        if (request.getDailySteps() != null) goal.setDailySteps(request.getDailySteps());
        if (request.getDailySleepH() != null) goal.setDailySleepH(request.getDailySleepH());
        if (request.getDailyWaterMl() != null) goal.setDailyWaterMl(request.getDailyWaterMl());
        healthGoalRepository.save(goal);

        user.setProfileComplete(true);
        userRepository.save(user);

        return Map.of("profileComplete", true, "message", "设置完成");
    }
}
