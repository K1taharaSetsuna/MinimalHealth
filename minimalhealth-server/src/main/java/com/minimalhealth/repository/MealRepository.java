package com.minimalhealth.repository;

import com.minimalhealth.model.entity.Meal;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.List;

public interface MealRepository extends JpaRepository<Meal, Long> {
    List<Meal> findByUserIdAndRecordDateOrderByCreatedAtAsc(Long userId, LocalDate recordDate);
}
