package com.oldp1e.sfp.dto.creditcard;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Schema(description = "DTO de resposta para uma transação de cartão")
public record CreditCardTransactionResponseDTO(

    @Schema(description = "ID único da transação", example = "550e8400-e29b-41d4-a716-446655440004")
    UUID id,

    @Schema(description = "ID do cartão de crédito", example = "550e8400-e29b-41d4-a716-446655440003")
    UUID creditCardId,

    @Schema(description = "Descrição", example = "Compra no supermercado")
    String description,

    @Schema(description = "Valor total da compra", example = "1200.00")
    BigDecimal totalAmount,

    @Schema(description = "Número total de parcelas", example = "3")
    Integer installments,

    @Schema(description = "Valor de cada parcela", example = "400.00")
    BigDecimal installmentValue,

    @Schema(description = "Parcela atual", example = "1")
    Integer currentInstallment,

    @Schema(description = "Data da compra", example = "2025-01-20")
    LocalDate purchaseDate,

    @Schema(description = "Data do primeiro vencimento", example = "2025-02-25")
    LocalDate firstDueDate,

    @Schema(description = "Data de criação", example = "2025-01-20T10:30:00")
    LocalDateTime createdAt

) { }
