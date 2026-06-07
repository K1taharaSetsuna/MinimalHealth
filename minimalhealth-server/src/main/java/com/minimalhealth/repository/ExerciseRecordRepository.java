package com.minimalhealth.repository;

import com.minimalhealth.model.entity.ExerciseRecord;
import com.minimalhealth.model.enums.ExerciseStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ExerciseRecordRepository extends JpaRepository<ExerciseRecord, Long> {
    List<ExerciseRecord> findByUserIdAndRecordDateBetweenOrderByRecordDateDesc(
        Long userId, LocalDate start, LocalDate end
    );

    Optional<ExerciseRecord> findByUserIdAndStatus(Long userId, ExerciseStatus status);

    @Query("SELECT COALESCE(SUM(e.calories), 0) FROM ExerciseRecord e WHERE e.user.id = :userId AND e.recordDate BETWEEN :start AND :end AND e.status = 'completed'")
    Integer sumCaloriesByUserIdAndDateBetween(
        @Param("userId") Long userId, @Param("start") LocalDate start, @Param("end") LocalDate end
    );

    @Query("SELECT COALESCE(SUM(e.distanceKm), 0) FROM ExerciseRecord e WHERE e.user.id = :userId AND e.recordDate BETWEEN :start AND :end AND e.status = 'completed'")
    Double sumDistanceByUserIdAndDateBetween(
        @Param("userId") Long userId, @Param("start") LocalDate start, @Param("end") LocalDate end
    );

    @Query("SELECT COUNT(e) FROM ExerciseRecord e WHERE e.user.id = :userId AND e.recordDate BETWEEN :start AND :end AND e.status = 'completed'")
    Integer countByUserIdAndDateBetween(
        @Param("userId") Long userId, @Param("start") LocalDate start, @Param("end") LocalDate end
    );
}
