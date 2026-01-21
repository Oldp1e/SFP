package com.oldp1e.sfp.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "simulations")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Simulation {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @Enumerated(EnumType.STRING)
    @Column(name = "simulation_type", nullable = false, length = 50)
    private SimulationType simulationType;

    @Column(name = "total_value", nullable = false, precision = 15, scale = 2)
    private BigDecimal totalValue;

    @Column(nullable = false)
    private Integer installments;

    @Column(name = "monthly_value", nullable = false, precision = 15, scale = 2)
    private BigDecimal monthlyValue;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "impact_on_balance", nullable = false, precision = 15, scale = 2)
    private BigDecimal impactOnBalance;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public enum SimulationType {
        INSTALLMENT,
        BUDGET
    }
}
