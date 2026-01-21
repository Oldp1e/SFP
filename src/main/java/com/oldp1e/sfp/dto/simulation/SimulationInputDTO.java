package com.oldp1e.sfp.dto.simulation;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDate;

@Schema(description = "DTO para criar uma simulação financeira")
public record SimulationInputDTO(

    @NotNull(message = "Tipo de simulação é obrigatório")
    @Schema(description = "Tipo de simulação", example = "INSTALLMENT", allowableValues = {"INSTALLMENT", "BUDGET"})
    String simulationType,

    @NotNull(message = "Valor total é obrigatório")
    @Positive(message = "Valor deve ser positivo")
    @Schema(description = "Valor total para simular", example = "2400.00")
    BigDecimal totalValue,

    @NotNull(message = "Número de parcelas é obrigatório")
    @Positive(message = "Deve ter pelo menos 1 parcela")
    @Schema(description = "Número de parcelas", example = "3")
    Integer installments,

    @NotNull(message = "Data inicial é obrigatória")
    @Schema(description = "Data de início da simulação", example = "2025-01-20")
    LocalDate startDate

) { }
