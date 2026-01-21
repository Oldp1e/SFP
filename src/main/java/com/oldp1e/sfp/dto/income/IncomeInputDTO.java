package com.oldp1e.sfp.dto.income;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Schema(description = "DTO para criar ou atualizar uma receita")
public record IncomeInputDTO(

    @NotNull(message = "ID da conta é obrigatório")
    @Schema(description = "ID da conta para esta receita", example = "550e8400-e29b-41d4-a716-446655440000")
    UUID accountId,

    @NotBlank(message = "Descrição é obrigatória")
    @Schema(description = "Descrição da receita (ex: Salário, VR, Extra)", example = "Salário Líquido")
    String description,

    @NotNull(message = "Valor é obrigatório")
    @Positive(message = "Valor deve ser positivo")
    @Schema(description = "Valor da receita", example = "3500.50")
    BigDecimal amount,

    @NotNull(message = "Tipo de receita é obrigatório")
    @Schema(description = "Tipo da receita", example = "SALARY", allowableValues = {"SALARY", "BENEFIT", "EXTRA"})
    String incomeType,

    @NotNull(message = "Data de referência é obrigatória")
    @Schema(description = "Data financeira da receita", example = "2025-01-20")
    LocalDate referenceDate

) { }
