package com.oldp1e.sfp.dto;

import com.oldp1e.sfp.entity.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * DTO para resposta com dados de usuário.
 *
 * @author SFP Team
 * @version 1.0.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "UserResponseDTO", description = "Dados de um usuário")
public class UserResponseDTO {

    @Schema(
            description = "ID único do usuário (UUID)",
            example = "123e4567-e89b-12d3-a456-426614174000"
    )
    private UUID id;

    @Schema(
            description = "Email do usuário",
            example = "usuario@example.com"
    )
    private String email;

    @Schema(
            description = "Nome completo do usuário",
            example = "João da Silva Santos"
    )
    private String fullName;

    @Schema(
            description = "Indica se o usuário está ativo",
            example = "true"
    )
    private Boolean isActive;

    @Schema(
            description = "Papel/permissão do usuário",
            example = "USER",
            allowableValues = {"USER", "ADMIN"}
    )
    private Role role;

    @Schema(
            description = "Data e hora de criação da conta",
            example = "2025-01-20T10:30:00"
    )
    private LocalDateTime createdAt;

    @Schema(
            description = "Data e hora do último login. Nulo se nunca fez login.",
            example = "2025-01-20T15:45:00"
    )
    private LocalDateTime lastLoginAt;
}


