package com.oldp1e.sfp.dto.expense;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Schema(description = "DTO de resposta para uma despesa")
public record ExpenseResponseDTO(

    @Schema(description = "ID único da despesa", example = "550e8400-e29b-41d4-a716-446655440002")
    UUID id,

    @Schema(description = "ID da conta", example = "550e8400-e29b-41d4-a716-446655440000")
    UUID accountId,

    @Schema(description = "Descrição", example = "Aluguel")
    String description,

    @Schema(description = "Valor", example = "1500.00")
    BigDecimal amount,

    @Schema(description = "Tipo de despesa", example = "FIXED")
    String expenseType,

    @Schema(description = "Data financeira", example = "2025-01-20")
    LocalDate referenceDate,

    @Schema(description = "Mês de referência (1-12)", example = "1")
    Integer referenceMonth,

    @Schema(description = "Ano de referência", example = "2025")
    Integer referenceYear,

    @Schema(description = "Data de criação", example = "2025-01-20T10:30:00")
    LocalDateTime createdAt

) { }
