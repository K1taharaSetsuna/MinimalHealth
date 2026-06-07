package com.minimalhealth.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Table(name = "reminder_setting")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReminderSetting {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Column(name = "water_enabled", nullable = false)
    @Builder.Default
    private Boolean waterEnabled = true;

    @Column(name = "water_times", nullable = false, length = 100)
    @Builder.Default
    private String waterTimes = "10:00,15:00,20:00";

    @Column(name = "sleep_enabled", nullable = false)
    @Builder.Default
    private Boolean sleepEnabled = true;

    @Column(name = "sleep_time", nullable = false)
    @Builder.Default
    private LocalTime sleepTime = LocalTime.of(22, 30);

    @Column(name = "exercise_enabled", nullable = false)
    @Builder.Default
    private Boolean exerciseEnabled = false;

    @Column(name = "exercise_time", nullable = false)
    @Builder.Default
    private LocalTime exerciseTime = LocalTime.of(7, 0);

    @Column(name = "created_at", nullable = false, updatable = false)
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at", nullable = false)
    @Builder.Default
    private LocalDateTime updatedAt = LocalDateTime.now();

    @PreUpdate
    public void preUpdate() { this.updatedAt = LocalDateTime.now(); }
}
