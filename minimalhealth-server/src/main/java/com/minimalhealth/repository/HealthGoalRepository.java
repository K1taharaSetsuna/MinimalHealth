package com.minimalhealth.repository;

import com.minimalhealth.model.entity.HealthGoal;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface HealthGoalRepository extends JpaRepository<HealthGoal, Long> {
    Optional<HealthGoal> findByUserId(Long userId);
}
