package com.minimalhealth.model.entity;

import com.minimalhealth.model.enums.ExerciseStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "exercise_record", indexes = {
    @Index(columnList = "user_id, record_date DESC")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExerciseRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "exercise_type", nullable = false, length = 50)
    private String exerciseType;

    @Column(name = "duration_min")
    private Integer durationMin;

    @Column(name = "distance_km")
    private Double distanceKm;

    private Integer calories;

    @Column(name = "avg_pace", length = 10)
    private String avgPace;

    @Column(name = "avg_hr")
    private Integer avgHr;

    @Column(name = "route_desc", length = 200)
    private String routeDesc;

    @Column(name = "record_date", nullable = false)
    private LocalDate recordDate;

    @Column(name = "started_at")
    private LocalDateTime startedAt;

    @Column(name = "ended_at")
    private LocalDateTime endedAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private ExerciseStatus status = ExerciseStatus.completed;

    @Column(name = "created_at", nullable = false, updatable = false)
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();
}
