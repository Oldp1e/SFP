package com.oldp1e.sfp.controller;

import com.oldp1e.sfp.dto.UserResponseDTO;
import com.oldp1e.sfp.entity.User;
import com.oldp1e.sfp.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller para endpoints de gerenciamento de usuários.
 *
 * @author SFP Team
 * @version 1.0.0
 */
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Tag(name = "Usuários", description = "Endpoints para gerenciamento de dados de usuários")
public class UserController {

    private final UserService userService;

    /**
     * Obtém os dados do usuário logado.
     * Requer autenticação via token JWT.
     *
     * @return ResponseEntity contendo dados do usuário autenticado
     */
    @GetMapping("/me")
    @Operation(
            summary = "Obter dados do usuário logado",
            description = "Retorna as informações do usuário autenticado. Requer token JWT válido no header Authorization.",
            security = @SecurityRequirement(name = "bearer-jwt"),
            tags = {"Usuários"}
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Dados do usuário retornados com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UserResponseDTO.class),
                            examples = @ExampleObject(
                                    name = "Exemplo de sucesso",
                                    value = "{\n" +
                                            "  \"id\": \"123e4567-e89b-12d3-a456-426614174000\",\n" +
                                            "  \"email\": \"usuario@example.com\",\n" +
                                            "  \"fullName\": \"João Silva\",\n" +
                                            "  \"isActive\": true,\n" +
                                            "  \"role\": \"USER\",\n" +
                                            "  \"createdAt\": \"2025-01-20T10:30:00\",\n" +
                                            "  \"lastLoginAt\": \"2025-01-20T15:45:00\"\n" +
                                            "}"
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Token JWT ausente ou inválido",
                    content = @Content(mediaType = "application/json")
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Token JWT expirado ou sem permissão",
                    content = @Content(mediaType = "application/json")
            )
    })
    public ResponseEntity<UserResponseDTO> getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication.getPrincipal() == null) {
            return ResponseEntity.status(401).build();
        }
        User user = (User) authentication.getPrincipal();
        UserResponseDTO response = userService.getUserById(user.getId());
        return ResponseEntity.ok(response);
    }
}
