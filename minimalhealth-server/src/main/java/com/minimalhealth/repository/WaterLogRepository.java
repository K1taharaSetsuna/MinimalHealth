package com.minimalhealth.repository;

import com.minimalhealth.model.entity.WaterLog;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.List;

public interface WaterLogRepository extends JpaRepository<WaterLog, Long> {
    List<WaterLog> findByUserIdAndRecordDateOrderByRecordedAtDesc(Long userId, LocalDate recordDate);
}
