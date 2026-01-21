package com.oldp1e.sfp.controller;

import com.oldp1e.sfp.dto.LoginRequestDTO;
import com.oldp1e.sfp.dto.LoginResponseDTO;
import com.oldp1e.sfp.dto.RegisterRequestDTO;
import com.oldp1e.sfp.dto.UserResponseDTO;
import com.oldp1e.sfp.service.AuthenticationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller para endpoints de autenticação e registro de usuários.
 *
 * @author SFP Team
 * @version 1.0.0
 */
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Autenticação", description = "Endpoints para login e registro de usuários")
public class AuthController {

    private final AuthenticationService authenticationService;

    /**
     * Realiza login do usuário com email e senha.
     *
     * @param request DTO contendo email e senha
     * @return ResponseEntity contendo token JWT e dados do usuário
     */
    @PostMapping("/login")
    @Operation(
            summary = "Realizar login",
            description = "Autentica um usuário existente e retorna um token JWT válido por 24 horas",
            tags = {"Autenticação"}
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Login realizado com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = LoginResponseDTO.class),
                            examples = @ExampleObject(
                                    name = "Exemplo de sucesso",
                                    value = "{\n" +
                                            "  \"token\": \"eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...\",\n" +
                                            "  \"type\": \"Bearer\",\n" +
                                            "  \"user\": {\n" +
                                            "    \"id\": \"123e4567-e89b-12d3-a456-426614174000\",\n" +
                                            "    \"email\": \"usuario@example.com\",\n" +
                                            "    \"fullName\": \"João Silva\",\n" +
                                            "    \"isActive\": true,\n" +
                                            "    \"role\": \"USER\",\n" +
                                            "    \"createdAt\": \"2025-01-20T10:30:00\",\n" +
                                            "    \"lastLoginAt\": \"2025-01-20T15:45:00\"\n" +
                                            "  }\n" +
                                            "}"
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Credenciais inválidas (email ou senha incorretos)",
                    content = @Content(mediaType = "application/json")
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Requisição inválida (email não é um email válido ou campo vazio)",
                    content = @Content(mediaType = "application/json")
            )
    })
    public ResponseEntity<LoginResponseDTO> login(@Valid @RequestBody LoginRequestDTO request) {
        try {
            LoginResponseDTO response = authenticationService.login(request);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    /**
     * Registra um novo usuário na plataforma.
     *
     * @param request DTO contendo email, senha, nome completo e role
     * @return ResponseEntity com status 201 (CREATED) contendo dados do novo usuário
     */
    @PostMapping("/register")
    @Operation(
            summary = "Registrar novo usuário",
            description = "Cria uma nova conta de usuário com email e senha. A senha deve ter no mínimo 6 caracteres.",
            tags = {"Autenticação"}
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Usuário registrado com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UserResponseDTO.class),
                            examples = @ExampleObject(
                                    name = "Exemplo de sucesso",
                                    value = "{\n" +
                                            "  \"id\": \"123e4567-e89b-12d3-a456-426614174000\",\n" +
                                            "  \"email\": \"novousuario@example.com\",\n" +
                                            "  \"fullName\": \"Maria Santos\",\n" +
                                            "  \"isActive\": true,\n" +
                                            "  \"role\": \"USER\",\n" +
                                            "  \"createdAt\": \"2025-01-20T10:30:00\",\n" +
                                            "  \"lastLoginAt\": null\n" +
                                            "}"
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Requisição inválida (email duplicado, senha muito curta, nome vazio, etc.)",
                    content = @Content(mediaType = "application/json")
            )
    })
    public ResponseEntity<UserResponseDTO> register(@Valid @RequestBody RegisterRequestDTO request) {
        try {
            UserResponseDTO response = authenticationService.register(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
}


