package com.oldp1e.sfp.dto;

import com.oldp1e.sfp.entity.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para requisição de registro de novo usuário.
 *
 * @author SFP Team
 * @version 1.0.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "RegisterRequestDTO", description = "Requisição para registro de novo usuário")
public class RegisterRequestDTO {

    @NotBlank(message = "Email é obrigatório")
    @Email(message = "Email inválido")
    @Schema(
            description = "Email do novo usuário (deve ser único)",
            example = "novousuario@example.com",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String email;

    @NotBlank(message = "Senha é obrigatória")
    @Size(min = 6, message = "Senha deve ter no mínimo 6 caracteres")
    @Schema(
            description = "Senha do usuário (mínimo 6 caracteres). Será criptografada no servidor.",
            example = "senhaSegura123",
            minLength = 6,
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String password;

    @NotBlank(message = "Nome completo é obrigatório")
    @Size(min = 3, max = 255, message = "Nome deve ter entre 3 e 255 caracteres")
    @Schema(
            description = "Nome completo do usuário",
            example = "João da Silva Santos",
            minLength = 3,
            maxLength = 255,
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String fullName;

    @NotNull(message = "Role é obrigatória")
    @Schema(
            description = "Papel/permissão do usuário (USER ou ADMIN)",
            example = "USER",
            requiredMode = Schema.RequiredMode.REQUIRED,
            allowableValues = {"USER", "ADMIN"}
    )
    private Role role;
}


