package com.minimalhealth.service;

import com.minimalhealth.model.entity.User;
import com.minimalhealth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public Map<String, Object> getProfile(Long userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("用户不存在"));

        Map<String, Object> profile = new LinkedHashMap<>();
        profile.put("id", user.getId());
        profile.put("name", user.getName());
        profile.put("email", user.getPhone() + "@email.com"); // mock email
        profile.put("phone", user.getPhone());
        profile.put("avatarInitial", user.getName() != null && !user.getName().isEmpty()
            ? user.getName().substring(0, 1) : "用");
        profile.put("gender", user.getGender());
        profile.put("birthDate", user.getBirthDate() != null ? user.getBirthDate().toString() : null);
        profile.put("heightCm", user.getHeightCm());
        profile.put("weightKg", user.getWeightKg());
        profile.put("activityLevel", user.getActivityLevel());
        profile.put("profileComplete", user.getProfileComplete());
        return profile;
    }

    public Map<String, Object> updateProfile(Long userId, String name, String email) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("用户不存在"));

        if (name != null) user.setName(name);
        userRepository.save(user);

        return getProfile(userId);
    }

    public Map<String, Object> updateBodyData(Long userId, Double heightCm, Double weightKg) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("用户不存在"));

        if (heightCm != null) user.setHeightCm(heightCm);
        if (weightKg != null) user.setWeightKg(weightKg);
        userRepository.save(user);

        return getProfile(userId);
    }
}
