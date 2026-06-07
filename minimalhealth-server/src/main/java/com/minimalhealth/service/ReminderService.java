package com.minimalhealth.service;

import com.minimalhealth.model.dto.request.ReminderUpdateRequest;
import com.minimalhealth.model.entity.ReminderSetting;
import com.minimalhealth.model.entity.User;
import com.minimalhealth.repository.ReminderSettingRepository;
import com.minimalhealth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
@RequiredArgsConstructor
public class ReminderService {

    private final ReminderSettingRepository reminderSettingRepository;
    private final UserRepository userRepository;

    public List<Map<String, Object>> getReminders(Long userId) {
        ReminderSetting settings = reminderSettingRepository.findByUserId(userId)
            .orElseGet(() -> createDefault(userId));

        DateTimeFormatter timeFmt = DateTimeFormatter.ofPattern("HH:mm");

        List<Map<String, Object>> reminders = new ArrayList<>();

        Map<String, Object> water = new LinkedHashMap<>();
        water.put("name", "饮水提醒");
        water.put("desc", "每日 " + settings.getWaterTimes());
        water.put("on", settings.getWaterEnabled());
        reminders.add(water);

        Map<String, Object> sleep = new LinkedHashMap<>();
        sleep.put("name", "睡眠提醒");
        sleep.put("desc", "每晚 " + settings.getSleepTime().format(timeFmt));
        sleep.put("on", settings.getSleepEnabled());
        reminders.add(sleep);

        Map<String, Object> exercise = new LinkedHashMap<>();
        exercise.put("name", "运动提醒");
        exercise.put("desc", "每日 " + settings.getExerciseTime().format(timeFmt));
        exercise.put("on", settings.getExerciseEnabled());
        reminders.add(exercise);

        return reminders;
    }

    public List<Map<String, Object>> updateReminders(Long userId, ReminderUpdateRequest request) {
        ReminderSetting settings = reminderSettingRepository.findByUserId(userId)
            .orElseGet(() -> createDefault(userId));

        if (request.getWaterEnabled() != null) settings.setWaterEnabled(request.getWaterEnabled());
        if (request.getSleepEnabled() != null) settings.setSleepEnabled(request.getSleepEnabled());
        if (request.getExerciseEnabled() != null) settings.setExerciseEnabled(request.getExerciseEnabled());
        reminderSettingRepository.save(settings);

        return getReminders(userId);
    }

    private ReminderSetting createDefault(Long userId) {
        User user = userRepository.findById(userId).orElse(null);
        return reminderSettingRepository.save(ReminderSetting.builder().user(user).build());
    }
}
