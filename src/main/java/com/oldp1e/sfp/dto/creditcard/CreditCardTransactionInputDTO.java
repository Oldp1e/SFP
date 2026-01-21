package com.oldp1e.sfp.dto.creditcard;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDate;

@Schema(description = "DTO para criar uma transação de cartão de crédito")
public record CreditCardTransactionInputDTO(

    @NotBlank(message = "Descrição é obrigatória")
    @Schema(description = "Descrição da transação", example = "Compra no supermercado")
    String description,

    @NotNull(message = "Valor total é obrigatório")
    @Positive(message = "Valor deve ser positivo")
    @Schema(description = "Valor total da compra", example = "1200.00")
    BigDecimal totalAmount,

    @NotNull(message = "Número de parcelas é obrigatório")
    @Positive(message = "Deve ter pelo menos 1 parcela")
    @Schema(description = "Número de parcelas (1+ para à vista ou parcelado)", example = "3")
    Integer installments,

    @NotNull(message = "Data de compra é obrigatória")
    @Schema(description = "Data da compra", example = "2025-01-20")
    LocalDate purchaseDate

) { }
