package com.minimalhealth.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "daily_stats", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"user_id", "record_date"})
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DailyStats {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "record_date", nullable = false)
    private LocalDate recordDate;

    @Column(nullable = false)
    @Builder.Default
    private Integer steps = 0;

    @Column(name = "resting_hr", nullable = false)
    @Builder.Default
    private Integer restingHr = 0;

    @Column(name = "sleep_hours", nullable = false)
    @Builder.Default
    private Double sleepHours = 0.0;

    @Column(name = "water_ml", nullable = false)
    @Builder.Default
    private Integer waterMl = 0;

    @Column(name = "calorie_intake", nullable = false)
    @Builder.Default
    private Integer calorieIntake = 0;

    @Column(name = "calorie_burned", nullable = false)
    @Builder.Default
    private Integer calorieBurned = 0;

    @Column(name = "health_score")
    private Integer healthScore;

    @Column(name = "created_at", nullable = false, updatable = false)
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at", nullable = false)
    @Builder.Default
    private LocalDateTime updatedAt = LocalDateTime.now();

    @PreUpdate
    public void preUpdate() { this.updatedAt = LocalDateTime.now(); }
}
