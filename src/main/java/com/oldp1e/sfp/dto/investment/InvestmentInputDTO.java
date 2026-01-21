package com.oldp1e.sfp.dto.investment;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDate;

@Schema(description = "DTO para criar ou atualizar um investimento")
public record InvestmentInputDTO(

    @NotBlank(message = "Descrição é obrigatória")
    @Schema(description = "Descrição do investimento (ex: Ação VALE, CDB Banco X)", example = "CDB Banco X")
    String description,

    @NotNull(message = "Valor é obrigatório")
    @Positive(message = "Valor deve ser positivo")
    @Schema(description = "Valor aplicado", example = "5000.00")
    BigDecimal amount,

    @NotBlank(message = "Tipo de investimento é obrigatório")
    @Schema(description = "Tipo do investimento", example = "CDB")
    String investmentType,

    @NotNull(message = "Data de referência é obrigatória")
    @Schema(description = "Data do investimento", example = "2025-01-20")
    LocalDate referenceDate

) { }
