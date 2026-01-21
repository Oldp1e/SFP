package com.oldp1e.sfp.dto.investment;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Schema(description = "DTO de resposta para um investimento")
public record InvestmentResponseDTO(

    @Schema(description = "ID único do investimento", example = "550e8400-e29b-41d4-a716-446655440005")
    UUID id,

    @Schema(description = "Descrição", example = "CDB Banco X")
    String description,

    @Schema(description = "Valor aplicado", example = "5000.00")
    BigDecimal amount,

    @Schema(description = "Tipo de investimento", example = "CDB")
    String investmentType,

    @Schema(description = "Data do investimento", example = "2025-01-20")
    LocalDate referenceDate,

    @Schema(description = "Data de criação", example = "2025-01-20T10:30:00")
    LocalDateTime createdAt

) { }
