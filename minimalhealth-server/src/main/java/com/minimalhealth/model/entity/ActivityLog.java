package com.minimalhealth.model.entity;

import com.minimalhealth.model.enums.ActivityType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "activity_log", indexes = {
    @Index(columnList = "user_id, record_date DESC, occurred_at DESC")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ActivityLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(name = "activity_type", nullable = false)
    private ActivityType activityType;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(length = 255)
    private String detail;

    @Column(name = "value_label", length = 50)
    private String valueLabel;

    @Column(name = "value_amount")
    private Double valueAmount;

    @Column(name = "value_unit", length = 20)
    private String valueUnit;

    @Column(name = "occurred_at", nullable = false)
    private LocalDateTime occurredAt;

    @Column(name = "record_date", nullable = false)
    private LocalDate recordDate;

    @Column(name = "created_at", nullable = false, updatable = false)
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();
}
