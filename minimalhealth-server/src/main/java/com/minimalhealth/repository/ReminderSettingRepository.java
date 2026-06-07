package com.minimalhealth.repository;

import com.minimalhealth.model.entity.ReminderSetting;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface ReminderSettingRepository extends JpaRepository<ReminderSetting, Long> {
    Optional<ReminderSetting> findByUserId(Long userId);
}
