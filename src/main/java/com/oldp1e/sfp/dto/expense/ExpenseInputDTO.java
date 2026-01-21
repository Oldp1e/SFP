package com.oldp1e.sfp.dto.expense;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Schema(description = "DTO para criar ou atualizar uma despesa")
public record ExpenseInputDTO(

    @NotNull(message = "ID da conta é obrigatório")
    @Schema(description = "ID da conta para esta despesa", example = "550e8400-e29b-41d4-a716-446655440000")
    UUID accountId,

    @NotBlank(message = "Descrição é obrigatória")
    @Schema(description = "Descrição da despesa", example = "Aluguel")
    String description,

    @NotNull(message = "Valor é obrigatório")
    @Positive(message = "Valor deve ser positivo")
    @Schema(description = "Valor da despesa", example = "1500.00")
    BigDecimal amount,

    @NotNull(message = "Tipo de despesa é obrigatório")
    @Schema(description = "Tipo da despesa", example = "FIXED", allowableValues = {"FIXED", "VARIABLE", "CREDIT_CARD", "INVESTMENT"})
    String expenseType,

    @NotNull(message = "Data de referência é obrigatória")
    @Schema(description = "Data financeira da despesa", example = "2025-01-20")
    LocalDate referenceDate

) { }
