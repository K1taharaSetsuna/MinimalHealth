package com.minimalhealth.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "water_log", indexes = {
    @Index(columnList = "user_id, record_date DESC, recorded_at DESC")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WaterLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "amount_ml", nullable = false)
    private Integer amountMl;

    @Column(name = "record_date", nullable = false)
    private LocalDate recordDate;

    @Column(name = "recorded_at", nullable = false)
    private LocalDateTime recordedAt;

    @Column(name = "created_at", nullable = false, updatable = false)
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();
}
