package com.oldp1e.sfp.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para requisição de login.
 *
 * @author SFP Team
 * @version 1.0.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "LoginRequestDTO", description = "Requisição para autenticação via email e senha")
public class LoginRequestDTO {

    @NotBlank(message = "Email é obrigatório")
    @Email(message = "Email inválido")
    @Schema(
            description = "Email do usuário para autenticação",
            example = "usuario@example.com",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String email;

    @NotBlank(message = "Senha é obrigatória")
    @Schema(
            description = "Senha do usuário (será validada contra hash armazenado)",
            example = "senhaSegura123",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String password;
}
