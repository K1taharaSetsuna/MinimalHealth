package com.minimalhealth.repository;

import com.minimalhealth.model.entity.ActivityLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.List;

public interface ActivityLogRepository extends JpaRepository<ActivityLog, Long> {
    List<ActivityLog> findByUserIdAndRecordDateOrderByOccurredAtDesc(Long userId, LocalDate recordDate);

    Page<ActivityLog> findByUserIdAndRecordDateOrderByOccurredAtDesc(Long userId, LocalDate recordDate, Pageable pageable);
}
