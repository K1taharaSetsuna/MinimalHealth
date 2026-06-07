package com.minimalhealth.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "health_goal")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HealthGoal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Column(name = "daily_steps", nullable = false)
    @Builder.Default
    private Integer dailySteps = 8000;

    @Column(name = "daily_sleep_h", nullable = false)
    @Builder.Default
    private Double dailySleepH = 8.0;

    @Column(name = "daily_water_ml", nullable = false)
    @Builder.Default
    private Integer dailyWaterMl = 2500;

    @Column(name = "created_at", nullable = false, updatable = false)
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at", nullable = false)
    @Builder.Default
    private LocalDateTime updatedAt = LocalDateTime.now();

    @PreUpdate
    public void preUpdate() { this.updatedAt = LocalDateTime.now(); }
}
