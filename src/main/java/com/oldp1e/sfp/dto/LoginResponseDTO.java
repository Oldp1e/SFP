package com.oldp1e.sfp.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para resposta de login contendo token JWT e dados do usuário.
 *
 * @author SFP Team
 * @version 1.0.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "LoginResponseDTO", description = "Resposta de autenticação com token JWT e dados do usuário")
public class LoginResponseDTO {

    @Schema(
            description = "Token JWT para autenticação em requisições subsequentes",
            example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJ1c3VhcmlvQGV4YW1wbGUuY29tIiwiaWF0IjoxNjExODI0MDAwLCJleHAiOjE2MTE5MTA0MDB9...",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String token;

    @Schema(
            description = "Tipo de autenticação utilizado",
            example = "Bearer",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String type = "Bearer";

    @Schema(
            description = "Dados do usuário autenticado",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private UserResponseDTO user;

    public LoginResponseDTO(String token, UserResponseDTO user) {
        this.token = token;
        this.user = user;
    }
}


