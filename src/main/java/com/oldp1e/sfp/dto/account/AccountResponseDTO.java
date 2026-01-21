package com.oldp1e.sfp.dto.account;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Schema(description = "DTO de resposta para uma conta")
public record AccountResponseDTO(

    @Schema(description = "ID único da conta", example = "550e8400-e29b-41d4-a716-446655440000")
    UUID id,

    @Schema(description = "Nome da conta", example = "Conta Principal")
    String name,

    @Schema(description = "Saldo inicial da conta", example = "5000.00")
    BigDecimal initialBalance,

    @Schema(description = "Data de criação", example = "2025-01-20T10:30:00")
    LocalDateTime createdAt

) { }
