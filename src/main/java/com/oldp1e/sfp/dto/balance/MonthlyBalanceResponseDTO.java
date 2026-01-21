package com.oldp1e.sfp.dto.balance;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Schema(description = "DTO de resposta para balanço mensal")
public record MonthlyBalanceResponseDTO(

    @Schema(description = "ID único do balanço", example = "550e8400-e29b-41d4-a716-446655440006")
    UUID id,

    @Schema(description = "Mês de referência (1-12)", example = "1")
    Integer referenceMonth,

    @Schema(description = "Ano de referência", example = "2025")
    Integer referenceYear,

    @Schema(description = "Soma total de entradas do mês", example = "5500.00")
    BigDecimal totalIncome,

    @Schema(description = "Soma total de saídas do mês", example = "2000.00")
    BigDecimal totalExpense,

    @Schema(description = "Balanço (receita - despesa)", example = "3500.00")
    BigDecimal balance,

    @Schema(description = "Data de criação", example = "2025-01-20T10:30:00")
    LocalDateTime createdAt

) { }
