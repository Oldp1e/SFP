package com.oldp1e.sfp.dto.simulation;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Schema(description = "DTO de resposta para uma simulação")
public record SimulationResponseDTO(

    @Schema(description = "ID único da simulação", example = "550e8400-e29b-41d4-a716-446655440007")
    UUID id,

    @Schema(description = "Tipo de simulação", example = "INSTALLMENT")
    String simulationType,

    @Schema(description = "Valor total simulado", example = "2400.00")
    BigDecimal totalValue,

    @Schema(description = "Número de parcelas", example = "3")
    Integer installments,

    @Schema(description = "Valor mensal", example = "800.00")
    BigDecimal monthlyValue,

    @Schema(description = "Data de início", example = "2025-01-20")
    LocalDate startDate,

    @Schema(description = "Impacto no balanço", example = "-2400.00")
    BigDecimal impactOnBalance,

    @Schema(description = "Data de criação", example = "2025-01-20T10:30:00")
    LocalDateTime createdAt

) { }
