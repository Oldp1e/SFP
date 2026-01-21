package com.oldp1e.sfp.dto.creditcard;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

@Schema(description = "DTO para criar ou atualizar um cartão de crédito")
public record CreditCardInputDTO(

    @NotBlank(message = "Nome do cartão é obrigatório")
    @Schema(description = "Nome do cartão (ex: Nubank, Itaú, C6)", example = "Nubank")
    String name,

    @NotNull(message = "Limite é obrigatório")
    @Positive(message = "Limite deve ser positivo")
    @Schema(description = "Limite de crédito", example = "10000.00")
    BigDecimal limitAmount,

    @NotNull(message = "Dia de fechamento é obrigatório")
    @Schema(description = "Dia do mês em que o cartão fecha (1-31)", example = "15")
    Integer closingDay,

    @NotNull(message = "Dia de vencimento é obrigatório")
    @Schema(description = "Dia do mês em que a fatura vence (1-31)", example = "25")
    Integer dueDay

) { }
