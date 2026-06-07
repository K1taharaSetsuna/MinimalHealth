package com.minimalhealth.repository;

import com.minimalhealth.model.entity.DailyStats;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface DailyStatsRepository extends JpaRepository<DailyStats, Long> {
    Optional<DailyStats> findByUserIdAndRecordDate(Long userId, LocalDate recordDate);

    @Query("SELECT d FROM DailyStats d WHERE d.user.id = :userId AND d.recordDate BETWEEN :start AND :end ORDER BY d.recordDate")
    List<DailyStats> findByUserIdAndRecordDateBetween(
        @Param("userId") Long userId,
        @Param("start") LocalDate start,
        @Param("end") LocalDate end
    );
}
