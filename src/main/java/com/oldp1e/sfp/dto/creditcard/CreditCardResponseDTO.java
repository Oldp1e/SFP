package com.oldp1e.sfp.dto.creditcard;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Schema(description = "DTO de resposta para um cartão de crédito")
public record CreditCardResponseDTO(

    @Schema(description = "ID único do cartão", example = "550e8400-e29b-41d4-a716-446655440003")
    UUID id,

    @Schema(description = "Nome do cartão", example = "Nubank")
    String name,

    @Schema(description = "Limite de crédito", example = "10000.00")
    BigDecimal limitAmount,

    @Schema(description = "Dia de fechamento", example = "15")
    Integer closingDay,

    @Schema(description = "Dia de vencimento", example = "25")
    Integer dueDay,

    @Schema(description = "Data de criação", example = "2025-01-20T10:30:00")
    LocalDateTime createdAt

) { }
