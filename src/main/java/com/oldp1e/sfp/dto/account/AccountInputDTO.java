package com.oldp1e.sfp.dto.account;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

@Schema(description = "DTO para criar ou atualizar uma conta")
public record AccountInputDTO(

    @NotBlank(message = "Nome da conta é obrigatório")
    @Schema(description = "Nome da conta (ex: Conta Principal, Poupança)", example = "Conta Principal")
    String name,

    @NotNull(message = "Saldo inicial é obrigatório")
    @Positive(message = "Saldo inicial deve ser positivo")
    @Schema(description = "Saldo inicial da conta", example = "5000.00")
    BigDecimal initialBalance

) { }
